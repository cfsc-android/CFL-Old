package com.xiandao.android.view.common;

/**
 * @author TanYong
 *         create at 2017/4/21 14:20
 */
public class LocalFile {
    private String originalUri;//原图URI
    private String thumbnailUri;//缩略图URI
    private int orientation;//图片旋转角度

    public String getThumbnailUri() {
        return thumbnailUri;
    }

    public void setThumbnailUri(String thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }

    public String getOriginalUri() {
        return originalUri;
    }

    public void setOriginalUri(String originalUri) {
        this.originalUri = originalUri;
    }


    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int exifOrientation) {
        orientation = exifOrientation;
    }
}
