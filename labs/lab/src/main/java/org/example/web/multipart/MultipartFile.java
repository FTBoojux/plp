package org.example.web.multipart;

public class MultipartFile {
    private String filename;
    private String contentType;
    private byte[] bytes;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "MultipartFile{" +
                "filename='" + filename + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
