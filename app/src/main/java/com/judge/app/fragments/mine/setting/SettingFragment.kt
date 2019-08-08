package com.judge.app.fragments.mine.setting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.fragmentViewModel
import com.judge.R
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.SettingItemBean
import com.judge.extensions.copy
import com.judge.settingItem
import com.judge.settingTitle
import com.vondear.rxtool.RxPhotoTool
import com.vondear.rxtool.RxTool
import com.vondear.rxui.view.dialog.RxDialogChooseImage
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import org.jetbrains.anko.collections.forEachWithIndex
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


data class SettingState(
    val items: List<SettingItemBean> = emptyList()
) : MvRxState

class SettingViewModel(
    initialState: SettingState
) : MvRxViewModel<SettingState>(initialState) {
    private val list = LinkedList<SettingItemBean>()

    init {
        getSettingTitles()
    }

    private fun getSettingTitles() {
        val titles = RxTool.getContext().resources.getStringArray(R.array.setting_item_title)
        titles.asIterable().forEachIndexed { _, name ->
            val item = SettingItemBean(
                title = name
            )
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

    companion object : MvRxViewModelFactory<SettingViewModel, SettingState> {
        override fun create(viewModelContext: ViewModelContext, state: SettingState): SettingViewModel? {
            return SettingViewModel(state)
        }
    }
}

class SettingFragment : BaseFragment() {
    private var resultUri: Uri? = null
    val viewModel: SettingViewModel by fragmentViewModel()
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        state.items.forEachWithIndex { index, item ->
            if (index == 0) {
                settingTitle {
                    id(item.title + index)
                    item(item)
                    onClick { _ ->
                        RxDialogChooseImage(this@SettingFragment, RxDialogChooseImage.LayoutType.TITLE).show()
                    }
                }
            } else {
                settingItem {
                    id(item.title + index)
                    item(item)
                    onClick { _ ->
                    }
                }
            }
        }
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
            }

            UCrop.REQUEST_CROP//UCrop裁剪之后的处理
            -> if (resultCode == Activity.RESULT_OK) {
                resultUri = data?.let { UCrop.getOutput(it) }
                resultUri?.let { viewModel.updatePhoto(it) }
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
        options.setStatusBarColor(ActivityCompat.getColor(RxTool.getContext(), R.color.colorPrimaryDark))

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
        //options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
        //options.setCropGridColor(Color.GREEN);
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