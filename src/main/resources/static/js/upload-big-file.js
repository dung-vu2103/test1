/**
 * Hàm xử lý upload file lớn bằng cách chia file thành các file nhỏ và upload từng file lên.
 * Input Params
 *   - inputFileID: thẻ file input chứa file cần upload.
 * Output: string
 *   - là thông tin relative path của file trên server
 * Note: Sau khi upload file thành công --> module tự động xóa clear dữ liệu file input.
 **/
async function process_upload(inputFileID) {
    let listChunks = [];
    let filePathResult = null;
    if($("#" + inputFileID).prop("files").length > 0) {
        let fileNeedUpload = $("#" + inputFileID).prop("files")[0];

        let fileName = fileNeedUpload.name;
        let fileSize = fileNeedUpload.size;
        let extension = getFileExtension(fileName);
        console.log("UPLOAD: fileName: " + fileName + "|fileSize: " + fileSize + "|extension: " + extension);
        var uploadId = initUploadRequest(fileName, fileSize, extension);
        if(uploadId != null) {
            console.log("uploadId is " + uploadId);
            const chunkSize = 10*1024*1024; // 100MB chunks (change as needed)
            let offset = 0;

            while (offset < fileNeedUpload.size) {
                listChunks.push(fileNeedUpload.slice(offset, offset + chunkSize));
                offset += chunkSize;
            }
            var i = 0;
            filePathResult = await uploadSmallChunks(uploadId, listChunks, fileSize);
            if(filePathResult != null) {
                $("#" + inputFileID).val(null);
            } else {
                console.log("filePathResult is null");
            }
        } else {
            console.log("uploadId is null");
        }
    } else {
        console.log("chua co file");
    }
    console.log("RETURN PATH: " + filePathResult);
    return filePathResult;
}

function initUploadRequest(fileName, fileSize, extension) {
    var uploadId = null;

    const formData = new FormData();
    formData.append("fileName", fileName);
    formData.append("fileSize", fileSize);
    formData.append("extension", extension);

    $.ajax({
        url: APPNAME + "/streamer/schedule/upload",
        type: 'POST',
        async: false,
        data: formData,
        success: function(data) {
            uploadId = data;
            console.log("RETURN UPLOAD-ID: " + uploadId);
        },
        processData: false,
        contentType: false
    });
    return uploadId;
}

async function uploadSmallChunks(uploadId, listChunks, totalSize) {
    let tmpPath = null;
    let uploadSized = 0;

    for (let chunkIndex = 0; chunkIndex < listChunks.length; chunkIndex++) {
        const smallChunk = listChunks[chunkIndex];
        const chunkFormData = new FormData();
        chunkFormData.append("uploadId", uploadId);
        chunkFormData.append("chunkIndex", chunkIndex); // Add chunk index here
        chunkFormData.append("chunkSize", smallChunk.size);
        chunkFormData.append("fileSize", totalSize);
        chunkFormData.append("fileChunkUpload", smallChunk);
        uploadSized = uploadSized + smallChunk.size;

        try {
            const data = await uploadChunk(chunkFormData);
            console.log("UPLOAD CHUNK RESPONSE: " + data);

            if (data.status === '0') {
                // Handle success
            } else if (data.status === '1') {
                console.log(data);
                tmpPath = data.value;
            } else {
                console.error("Chunk upload error:", data);
                alert("Chunk upload failed.");
            }

            const percent = Math.round((uploadSized / totalSize) * 100);
            console.log("Index: " + chunkIndex + " percentage: " + percent);
            $("#uploadProgressBar").css('width', percent + '%');
        } catch (error) {
            console.error("Chunk upload error:", error);
            alert("Chunk upload failed.");
        }
    }

    console.info("VIDEO_PATH: " + tmpPath);
    return tmpPath;
}

function uploadChunk(chunkFormData) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: APPNAME + "/streamer/schedule/upload-chunk",
            type: 'POST',
            data: chunkFormData,
            success: function(data) {
                resolve(data);
            },
            error: function(error) {
                reject(error);
            },
            processData: false,
            contentType: false
        });
    });
}

function getFileExtension(filename) {
    const parts = filename.split('.');
    if (parts.length > 1) {
        return parts.pop();
    }
    return ""; // Trả về chuỗi rỗng nếu không tìm thấy phần mở rộng
}