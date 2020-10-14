package com.example.test2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;

public class MyUtil {


    /*
     * 获取位图的RGB数据
     */
    public static byte[] getRGBByBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int size = width * height;

        int pixels[] = new int[size];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        byte[] data = convertColorToByte(pixels);


        return data;
    }


    /*
     * 像素数组转化为RGB数组
     */
    public static byte[] convertColorToByte(int color[]) {
        if (color == null) {
            return null;
        }

        byte[] data = new byte[color.length * 3 * 2];
        for (int i = 0; i < color.length; i++) {
            data[i * 3] = (byte) (color[i] & 0xff);
            data[i * 3 + 1] = (byte) (color[i] >> 8 & 0xff);
            data[i * 3 + 2] = (byte) (color[i] >> 16 & 0xff);
        }

        return data;

    }

    // 等比缩放图片
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(-scaleWidth, scaleHeight);//缩小，镜像
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    //中心点旋转图片
    public static Bitmap rotateImg(Bitmap bm, float degrees) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, (float) width / 2, (float) height / 2);//旋转
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    //打印9*9一寸照片，bitmap创建
    public static Bitmap createBitmap(Bitmap bm) {
        int width = 1824;//创建图片的宽高
        int heigth = 1216;
        int row = 3;//需要打印的行列数
        int col = 3;
        int w = bm.getWidth();//传入图片的宽高
        int h = bm.getHeight();

        int dW = (width - w * row) / (row * 2);//每个图片的上下左右间距平均值
        int dH = (heigth - h * col) / (col * 2);

        Bitmap newBitmap = Bitmap.createBitmap(width, heigth, Bitmap.Config.ARGB_8888);
        newBitmap.eraseColor(Color.parseColor("#ffffff"));//填充背景白色
        Canvas cv = new Canvas(newBitmap);

        for (int i = 0; i < row; i++) {//绘出行照片
            for (int j = 0; j < col; j++) {//绘出列照片
                cv.drawBitmap(bm, w * i + (i + 2) * dW, h * j + (j + 2) * dH, null);
            }
        }
        return newBitmap;
    }
}
