package org.example.handler;

import org.example.enums.HTTPEnum;
import org.example.web.RequestHandler;
import org.example.web.multipart.MultipartFile;
import org.example.web.request.HttpRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUploadHandler implements RequestHandler<Void> {
    private static final String tempDir = "D:\\tmp\\";
    @Override
    public Object doHandle(HttpRequest<Void> httpRequest) {
        MultipartFile file = httpRequest.getFormData().getMultipartFile("file");
        String tempFile = tempDir
                + System.currentTimeMillis() +
                file.getFilename().substring(file.getFilename().lastIndexOf('.'));
        File fileTarget = new File(tempFile);
        fileTarget.deleteOnExit();
        try {
            FileOutputStream outputStream = new FileOutputStream(fileTarget);
            outputStream.write(file.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "ok";
    }

    @Override
    public String getUrl() {
        return "fileUpload";
    }

    @Override
    public String getType() {
        return HTTPEnum.POST.type;
    }
}
