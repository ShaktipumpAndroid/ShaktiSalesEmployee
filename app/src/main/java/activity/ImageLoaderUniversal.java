package activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.concurrent.atomic.AtomicReference;

@SuppressLint("NewApi")
public class ImageLoaderUniversal {

    public static final DisplayImageOptions ROUNDED_THUMB_OPTIONS = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.media_loader)
            .displayer(new RoundedBitmapDisplayer(10)).cacheInMemory(true).cacheOnDisc(true).considerExifParams(true).build();
    public static final AtomicReference<DisplayImageOptions> option_normal_Image = new AtomicReference<>(new Builder().showImageOnLoading(R.drawable.media_loader)
            .showImageForEmptyUri(R.drawable.media_loader).showImageOnFail(R.drawable.media_loader).cacheInMemory(true).cacheOnDisc(true)
            .considerExifParams(true).build());
    public static DisplayImageOptions option_normal_Image4 = new Builder().showImageOnLoading(R.drawable.media_loader)
            .showImageForEmptyUri(R.drawable.media_loader).showImageOnFail(R.drawable.media_loader).cacheInMemory(true).cacheOnDisc()
            .considerExifParams(true).build();
    public static Builder option_Round_Image = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.media_loader)
            .displayer(new RoundedBitmapDisplayer(1000)).cacheInMemory(true).cacheOnDisc(true).considerExifParams(true);
    public static DisplayImageOptions option_normal_Image_Thumbnail = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.media_loader)
            // .postProcessor for decreasing size of downloaded bitmap afer
            // lazzy loading
            .postProcessor(new BitmapProcessor() {
                @Override
                public Bitmap process(Bitmap bmp) {
                    int sizeOfBitmap = sizeOf(bmp);
                    System.out.println("Size of bitmap-" + sizeOfBitmap);

                    int divider = 1;
                    if (sizeOfBitmap <= 200) {
                        divider = 1;
                    } else if (sizeOfBitmap > 200 && sizeOfBitmap <= 400) {
                        divider = 2;
                    } else if (sizeOfBitmap > 400 && sizeOfBitmap <= 800) {
                        divider = 3;
                    } else if (sizeOfBitmap > 8000 && sizeOfBitmap <= 1200) {
                        divider = 4;
                    } else if (sizeOfBitmap > 1200 && sizeOfBitmap <= 1700) {
                        divider = 5;
                    } else if (sizeOfBitmap > 1700 && sizeOfBitmap <= 3000) {
                        divider = 6;
                    } else if (sizeOfBitmap > 3000 && sizeOfBitmap <= 4500) {
                        divider = 7;
                    } else {
                        divider = 8;
                    }

                    return Bitmap.createScaledBitmap(bmp, bmp.getWidth() / divider, bmp.getHeight() / divider, true);
                }
            }).showImageForEmptyUri(R.drawable.media_loader).showImageOnFail(R.drawable.media_loader).cacheInMemory(true).cacheOnDisc()
            .considerExifParams(true).build();
    public static DisplayImageOptions option_normal_Image_Thumbnail_cover = new DisplayImageOptions.Builder()
            .showImageOnLoading(null)
            // .postProcessor for decreasing size of downloaded bitmap afer
            // lazzy loading
            .postProcessor(new BitmapProcessor() {
                @Override
                public Bitmap process(Bitmap bmp) {
                    int sizeOfBitmap = sizeOf(bmp);
                    System.out.println("Size of bitmap-" + sizeOfBitmap);

                    int divider = 1;
                    if (sizeOfBitmap <= 200) {
                        divider = 1;
                    } else if (sizeOfBitmap > 200 && sizeOfBitmap <= 400) {
                        divider = 2;
                    } else if (sizeOfBitmap > 400 && sizeOfBitmap <= 800) {
                        divider = 3;
                    } else if (sizeOfBitmap > 8000 && sizeOfBitmap <= 1200) {
                        divider = 4;
                    } else if (sizeOfBitmap > 1200 && sizeOfBitmap <= 1700) {
                        divider = 5;
                    } else if (sizeOfBitmap > 1700 && sizeOfBitmap <= 3000) {
                        divider = 6;
                    } else if (sizeOfBitmap > 3000 && sizeOfBitmap <= 4500) {
                        divider = 7;
                    } else {
                        divider = 8;
                    }

                    return Bitmap.createScaledBitmap(bmp, bmp.getWidth() / divider, bmp.getHeight() / divider, true);
                }
            }).showImageForEmptyUri(null).showImageOnFail(null).cacheInMemory(true).cacheOnDisc()
            .considerExifParams(true).build();
    public static DisplayImageOptions DiaplayOptionForProgresser = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.media_loader).showImageOnFail(R.drawable.media_loader).cacheInMemory(true)
            .resetViewBeforeLoading(true).cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true).build();
    static ImageLoader imageLoader;
    static ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    @SuppressLint("NewApi")
    public static int sizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight() / 1000;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return data.getByteCount() / 1000;
        } else {
            return data.getAllocationByteCount() / 1000;
        }
    }

    public static void ImageLoadRound(Context mContext, String URl, ImageView imgView, Builder Display_options) {
        if (!TextUtils.isEmpty(URl)) {
            Display_options.showImageOnLoading(R.drawable.media_loader).showImageForEmptyUri(R.drawable.media_loader)
                    .showImageOnFail(R.drawable.media_loader);

            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(mContext)
                    .defaultDisplayImageOptions(Display_options.build());

            ImageLoaderConfiguration config = builder.build();
            imageLoader = ImageLoader.getInstance();
            if (!imageLoader.isInited()) {
                imageLoader.init(config.createDefault(mContext));
            }

            imageLoader.displayImage(URl, imgView, Display_options.build(), animateFirstListener);
        }
    }


    public static void ImageLoadSquare(Context mContext, String URl, ImageView imgView, DisplayImageOptions Display_options) {
        if (!TextUtils.isEmpty(URl)) {
            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(mContext).defaultDisplayImageOptions(
                    Display_options).memoryCache(new WeakMemoryCache());

            ImageLoaderConfiguration config = builder.build();
            imageLoader = ImageLoader.getInstance();
            if (!imageLoader.isInited()) {
                imageLoader.init(config.createDefault(mContext));
            }

            imageLoader.displayImage(URl, imgView, Display_options, animateFirstListener);
        }
    }

    public static void ImageLoadSquareWithProgressBar(Context mContext, String URl, ImageView imgView,
                                                      DisplayImageOptions displayImageOptions, final ProgressBar mLoader) {

        if (!TextUtils.isEmpty(URl)) {

            imageLoader.displayImage(URl, imgView, displayImageOptions, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            if (null != mLoader) {
                                mLoader.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            @SuppressWarnings("unused")
                            String message = null;
                            switch (failReason.getType()) {
                                case IO_ERROR:
                                    message = "Input/Output error";
                                    break;
                                case DECODING_ERROR:
                                    message = "Image can't be decoded";
                                    break;
                                case NETWORK_DENIED:
                                    message = "Downloads are denied";
                                    break;
                                case OUT_OF_MEMORY:
                                    message = "Out Of Memory error";
                                    break;
                                case UNKNOWN:
                                    message = "Unknown error";
                                    break;
                            }
                            if (null != mLoader) {
                                mLoader.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            mLoader.setVisibility(View.GONE);
                        }
                    }

            );
        }
    }

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }

}
