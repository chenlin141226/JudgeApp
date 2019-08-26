package com.judge.app.fragments.mine.setting

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.airbnb.mvrx.*
import com.jeremyliao.liveeventbus.LiveEventBus
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.ProfileUpdateResultBean
import com.judge.data.bean.SettingItemBean
import com.judge.data.bean.UpLoadPhotoResultBean
import com.judge.data.repository.MineRepository
import com.judge.extensions.copy
import com.judge.network.JsonResponse
import com.judge.settingItem
import com.judge.settingTitle
import com.judge.utils.LogUtils
import com.judge.views.BottomPopupViewList
import com.lxj.xpopup.interfaces.OnSelectListener
import com.tbruyelle.rxpermissions2.RxPermissions
import com.vondear.rxtool.RxPermissionsTool
import com.vondear.rxtool.RxPhotoTool
import com.vondear.rxtool.RxTool
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.support.v4.toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


data class SettingState(
    val items: List<SettingItemBean> = emptyList(),
    val resultBean: UpLoadPhotoResultBean? = null,
    val updateProfileResult: Async<JsonResponse<ProfileUpdateResultBean>> = Uninitialized
) : MvRxState

class SettingViewModel(
    initialState: SettingState
) : MvRxViewModel<SettingState>(initialState) {
    private val list = LinkedList<SettingItemBean>()
    val bottomList: Array<String> =
        RxTool.getContext().resources.getStringArray(R.array.setting_bottom_list)
    private val map = hashMapOf("version" to "4", "module" to "uploadavatar")

    init {
        getSettingTitles()
    }

    private fun getSettingTitles() {
        val titles = RxTool.getContext().resources.getStringArray(R.array.setting_item_title)
        val profileDetail = MineRepository.userProfile?.space
        titles.asIterable().forEachIndexed { index, name ->
            val item = when (index) {
                0 -> SettingItemBean(
                    title = name,
                    photoUrl = MineRepository.userProfile?.member_avatar ?: ""
                )
                1 -> SettingItemBean(title = name, content = profileDetail?.realname ?: "")
                2 -> SettingItemBean(
                    title = name, content = when (profileDetail?.gender) {
                        "0" -> "保密"
                        "1" -> "男"
                        "2" -> "女"
                        else -> ""
                    }
                )
                3 -> SettingItemBean(title = name, content = profileDetail?.birthdate ?: "")
                4 -> SettingItemBean(title = name, content = profileDetail?.address ?: "")
                5 -> SettingItemBean(title = name, content = profileDetail?.mobile ?: "")
                6 -> SettingItemBean(title = name, content = profileDetail?.qq ?: "")
                else -> SettingItemBean(title = "")
            }
            list.add(item)
        }

        setState {
            copy(items = list)
        }
    }

    fun updatePhoto(uri: Uri) {
        setState {
            copy(items = items.copy(0, items[0].copy(photoUri = uri, photoUrl = "")))
        }
    }

    fun upLoadPhoto(file: File) {
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("Filedata", file.name, requestFile)
        MineRepository.upLoadPhoto(body, map)
            .subscribeOn(Schedulers.io()).execute {
                copy(resultBean = it()?.Variables)
            }
    }

    fun updateItem(settingArgs: SettingArgs) {
        setState {
            copy(
                items = items.copy(
                    settingArgs.index,
                    items[settingArgs.index].copy(content = settingArgs.content)
                )
            )
        }
    }

    fun updateProfile(settingArgs: SettingArgs) = withState { state ->
        if (state.updateProfileResult is Loading) return@withState
        val map = hashMapOf("zen_submit" to "1")
        when (settingArgs.index) {
            1 -> {
                map["realname"] = settingArgs.content
                map["privacy-realname"] = "0"
            }
            2 -> {
                map["gender"] = if (settingArgs.content == "男") "1" else "2"
                map["privacy-gender"] = "0"
            }
            3 -> {
                map["birthdate"] = settingArgs.content
                map["privacy-birthdate"] = "0"
            }
            4 -> {
                map["address"] = settingArgs.content
                map["privacy-address"] = "0"
            }
            5 -> {
                map["mobile"] = settingArgs.content
                map["privacy-mobile"] = "0"
            }
            6 -> {
                map["qq"] = settingArgs.content
                map["privacy-qq"] = "0"
            }
            else -> {
            }
        }
        MineRepository.updateProfile(map).subscribeOn(Schedulers.io())
            .doOnError {
                it.message?.let { it1 -> LogUtils.e(it1) }
            }
            .execute {
                copy(updateProfileResult = it)
            }
    }

    companion object : MvRxViewModelFactory<SettingViewModel, SettingState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SettingState
        ): SettingViewModel? {
            return SettingViewModel(state)
        }
    }
}

class SettingFragment : BaseFragment() {
    private var resultUri: Uri? = null
    val viewModel: SettingViewModel by fragmentViewModel()
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private val list = LinkedList<SettingItemBean>()
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        list.clear()
        state.items.forEachWithIndex { index, item ->
            list.add(item)
            if (index == 0) {
                settingTitle {
                    id(item.title + index)
                    item(item)
                    onClick { _ ->
                        RxPermissionsTool.with(activity).initPermission()
                        //RxDialogChooseImage(this@SettingFragment, RxDialogChooseImage.LayoutType.TITLE).show()
                        BottomPopupViewList(context!!, viewModel.bottomList)
                            .setOnSelectListener(OnSelectListener { position, _ ->
                                when (position) {
                                    0 -> {
                                        RxPermissions(this@SettingFragment).request(
                                            Manifest.permission.CAMERA
                                        ).subscribe {
                                            if (it) {
                                                RxPhotoTool.openCameraImage(this@SettingFragment)
                                            } else {
                                                toast("请打开相机权限！")
                                            }
                                        }
                                    }
                                    1 -> RxPhotoTool.openLocalImage(this@SettingFragment)
                                }
                            }).showPopup()
                    }
                }
            } else {
                settingItem {
                    id(item.title + index)
                    item(item)
                    onClick { _ ->
                        val settingArgs = SettingArgs(index, list[index].content)
                        when (index) {
                            1 -> navigateTo(
                                R.id.action_settingFragment_to_editNameFragment,
                                settingArgs
                            )
                            2 -> navigateTo(
                                R.id.action_settingFragment_to_editGenderFragment,
                                settingArgs
                            )
                            3 -> navigateTo(
                                R.id.action_settingFragment_to_editBirthdayFragment,
                                settingArgs
                            )
                            4 -> navigateTo(
                                R.id.action_settingFragment_to_editAddressFragment,
                                settingArgs
                            )
                            5 -> navigateTo(
                                R.id.action_settingFragment_to_editPhoneNumberFragment,
                                settingArgs
                            )
                            6 -> navigateTo(
                                R.id.action_settingFragment_to_editQQFragment,
                                settingArgs
                            )
                        }
                    }
                }
            }

        }
    }

    override fun initData() {
        super.initData()
        LiveEventBus.get().with("setting", SettingArgs::class.java)
            .observe(this@SettingFragment, Observer<SettingArgs> {
                viewModel.updateItem(it)
                viewModel.updateProfile(it)
            })
        viewModel.asyncSubscribe(SettingState::updateProfileResult, onSuccess = {
            LogUtils.e(it.Variables.data.toString())
            LiveEventBus.get().with("updateProfile").post(true)
        }, onFail = {
            LogUtils.e(it.localizedMessage ?: "error")
        })
    }

    override fun initView() {
        super.initView()
        toolbar.isVisible = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RxPhotoTool.GET_IMAGE_FROM_PHONE//选择相册之后的处理
            -> if (resultCode == Activity.RESULT_OK) {
                //                    RxPhotoTool.cropImage(ActivityUser.this, );// 裁剪图片
                initUCrop(data?.data)
            }
            RxPhotoTool.GET_IMAGE_BY_CAMERA//选择照相机之后的处理
            -> if (resultCode == Activity.RESULT_OK) {
                /* data.getExtras().get("data");*/
                //                    RxPhotoTool.cropImage(ActivityUser.this, RxPhotoTool.imageUriFromCamera);// 裁剪图片
                initUCrop(RxPhotoTool.imageUriFromCamera)
            }
            RxPhotoTool.CROP_IMAGE//普通裁剪后的处理
            -> {
                viewModel.updatePhoto(RxPhotoTool.cropImageUri)
                viewModel.upLoadPhoto(
                    File(
                        RxPhotoTool.getImageAbsolutePath(
                            context,
                            RxPhotoTool.cropImageUri
                        )
                    )
                )
            }

            UCrop.REQUEST_CROP//UCrop裁剪之后的处理
            -> if (resultCode == Activity.RESULT_OK) {
                resultUri = data?.let { UCrop.getOutput(it) }
                resultUri?.let { viewModel.updatePhoto(it) }
                viewModel.upLoadPhoto(File(RxPhotoTool.getImageAbsolutePath(context, resultUri)))
            }
        }
    }

    private fun initUCrop(uri: Uri?) {
        val timeFormatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
        val time = System.currentTimeMillis()
        val imageName = timeFormatter.format(Date(time))

        val destinationUri = Uri.fromFile(File(context?.cacheDir, "$imageName.jpeg"))

        val options = UCrop.Options()
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL)
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(RxTool.getContext(), R.color.colorPrimary))
        //设置状态栏颜色
        options.setStatusBarColor(
            ActivityCompat.getColor(
                RxTool.getContext(),
                R.color.colorPrimaryDark
            )
        )

        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5f)
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666)
        //设置裁剪窗口是否为椭圆
        options.setCircleDimmedLayer(true)
        //设置是否展示矩形裁剪框
        options.setShowCropFrame(false)
        //设置裁剪框横竖线的宽度
        options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
        options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
        options.setCropGridColumnCount(0)
        //设置横线的数量
        options.setCropGridRowCount(0)

        UCrop.of(uri!!, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1000, 1000)
            .withOptions(options)
            .start(RxTool.getContext(), this)
    }
}