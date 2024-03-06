document.addEventListener("DOMContentLoaded", function () {
    const uploadForm = document.getElementById("form-update-live");
    const titleInput = document.getElementById("title");
    const descriptionInput = document.getElementById("description");
    const enableChat = document.getElementById("enable_chat");
    const privacy = document.getElementById("privacy");
    const timeEventStartInput = document.getElementById("time_event_start");
    const fileAvatar = document.getElementById("file1");
    const fileInput = document.getElementById("videoUpload");
    const submitButton = document.getElementById("submitButton");
    const progressPopup = document.getElementById("progressPopup");
    const progressBar = document.getElementById("progressBar");
    const progressText = document.getElementById("progressText");
    const sucsessPopup = document.getElementById("sucsessPopup");
    const setTimePopup = document.getElementById("setTimePopup");
    const submitTimeButton = document.getElementById("continue-create");
    const videoSrc = document.getElementById("here");

    let fileChunks = [];
    let currentChunkIndex = 0;
    let uploadId = null;
    let videoPath = "";

    uploadForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Prevent default form submission

        const file = fileInput ? fileInput.files[0] : null;

        if (!file || !videoPath || !fileAvatar.value) {
            // None of the file inputs have data, proceed to the final API call
            openPopupSetTime(videoSrc.src); // Pass null as videoPath since no video to upload
            return;
        } else {
            const formData = new FormData();
            formData.append("fileName", file.name);
            formData.append("fileSize", file.size);
            formData.append("extension", getFileExtension(file.name));

            openPopup();

            // Step 1: Request uploadId from the /upload API using XMLHttpRequest
            const xhrUpload = new XMLHttpRequest();
            xhrUpload.open("POST", "upload", true);
            xhrUpload.onreadystatechange = function () {
                if (xhrUpload.readyState === XMLHttpRequest.DONE) {
                    if (xhrUpload.status === 200) {
                        uploadId = xhrUpload.responseText;

                        // Start the video upload process
                        prepareFileChunks(file);
                        uploadChunks(formData, file);
                    } else {
                        closePopup();
                        console.error("Upload initiation error:", xhrUpload.status);
                        alert("Upload initiation failed.");
                    }
                }
            };
            xhrUpload.send(formData);
        }
    });

    function getFileExtension(filename) {
        // return filename.split('.').pop();
        const parts = filename.split('.');
        if (parts.length > 1) {
            return parts.pop();
        }
        return ""; // Trả về chuỗi rỗng nếu không tìm thấy phần mở rộng
    }

    function openPopup() {
        progressPopup.style.display = "block";
        submitButton.style.display = "none";
    }

    function closePopup() {
        progressPopup.style.display = "none";
        sucsessPopup.style.display = "block";
    }

    function prepareFileChunks(file) {
        const chunkSize = 10 * 1024 * 1024; // 10MB chunks (change as needed)
        let offset = 0;

        while (offset < file.size) {
            fileChunks.push(file.slice(offset, offset + chunkSize));
            offset += chunkSize;
        }
    }

    function uploadChunks(formData, file) {
        if (currentChunkIndex >= fileChunks.length) {
            // All chunks uploaded
            closePopup();
            return;
        }

        const chunk = fileChunks[currentChunkIndex];

        if (!chunk) {
            closePopup();
            console.error("Chunk is undefined at index:", currentChunkIndex);
            alert("Chunk is undefined.");
            return;
        }

        const chunkFormData = new FormData();
        chunkFormData.append("uploadId", uploadId);
        chunkFormData.append("chunkIndex", currentChunkIndex); // Add chunk index here
        chunkFormData.append("chunkSize", chunk.size);
        chunkFormData.append("fileSize", file.size);
        chunkFormData.append("fileChunkUpload", chunk);

        // Step 2: Upload chunk to the /upload-chunk API using XMLHttpRequest
        const xhrUploadChunk = new XMLHttpRequest();
        xhrUploadChunk.open("POST", "upload-chunk", true);
        xhrUploadChunk.onreadystatechange = function () {
            if (xhrUploadChunk.readyState === XMLHttpRequest.DONE) {
                if (xhrUploadChunk.status === 200) {
                    console.log(xhrUploadChunk.response)
                    var result = JSON.parse(xhrUploadChunk.response);

                    if (result.status === '0') {
                        console.log(result.value);
                        currentChunkIndex++;
                        const progress = (currentChunkIndex / fileChunks.length) * 100;
                        progressBar.value = progress;
                        progressText.textContent = `${progress.toFixed(2)}%`;

                        // Upload the next chunk
                        uploadChunks(formData, file);
                    }else if (result.status === '1') {
                        // Assume result is a path
                        console.log(result.value);
                        videoPath = result.value; // Assign the video path to the input
                        openPopupSetTime(videoPath);
                        // uploadFile(videoPath); // Continue uploading the next chunks
                    } else {
                        closePopup();
                        console.error("Chunk upload error:", xhrUploadChunk.status);
                        alert("Chunk upload failed.");
                    }
                }
            }
        };
        xhrUploadChunk.send(chunkFormData);
    }

    function openPopupSetTime(videoPath) {
        setTimePopup.style.display = "block";

        submitTimeButton.addEventListener("click", function() {
            var enteredTime = timeEventStartInput.value;
            setTimePopup.style.display = "none"; // Ẩn popup sau khi xử lý
            uploadFile(videoPath, enteredTime)
        });
    }

    // Step 3: Send formData to /save-video-feature using XMLHttpRequest
    function uploadFile(videoPath, enteredTime) {

        const formData = new FormData();
        formData.append("video_src", videoPath);
        formData.append("title", titleInput.value);
        formData.append("description", descriptionInput.value);
        formData.append("enable_chat", enableChat.value);
        formData.append("privacy", privacy.value);
        formData.append("time_event_start", enteredTime);
        formData.append("thumbUpload", fileAvatar.value);

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "", true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    closePopup();
                    const redirectUrl  = xhr.responseText;
                    if (redirectUrl) {
                        window.location.href = '/' + window.location.pathname.split('/')[1] + redirectUrl; // Thực hiện chuyển hướng tới trang web
                    } else {
                        alert("Upload successful!");
                    }
                } else {
                    closePopup();
                    alert("Upload failed.");
                }
            }
        };

        xhr.send(formData);
    }
});
