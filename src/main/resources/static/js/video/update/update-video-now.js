document.addEventListener("DOMContentLoaded", function () {
    const uploadForm = document.getElementById("form-update-live");
    const titleInput = document.getElementById("title");
    const descriptionInput = document.getElementById("description");
    const enableChat = document.getElementById("enable_chat");
    const privacy = document.getElementById("privacy");
    const fileAvatar = document.getElementById("file1");
    const sucsessPopup = document.getElementById("sucsessPopup");
    const videoSrc = document.getElementById("here");

    uploadForm.addEventListener("submit", function (event) {
        event.preventDefault();
        uploadFile(videoSrc.src)
    });

    function openPopup() {
        sucsessPopup.style.display = "block";
        submitButton.style.display = "none";
    }

    // Step 3: Send formData to /save-video-feature using XMLHttpRequest
    function uploadFile(videoPath) {

        const formData = new FormData();
        formData.append("video_src", videoPath);
        formData.append("title", titleInput.value);
        formData.append("description", descriptionInput.value);
        formData.append("enable_chat", enableChat.value);
        formData.append("privacy", privacy.value);
        formData.append("thumbUpload", fileAvatar.value);

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "", true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    openPopup();
                    const redirectUrl  = xhr.responseText;
                    if (redirectUrl) {
                        window.location.href = '/' + window.location.pathname.split('/')[1] + redirectUrl; // Thực hiện chuyển hướng tới trang web
                    } else {
                        alert("Upload successful!");
                    }
                } else {
                    alert("Upload failed.");
                }
            }
        };
        xhr.send(formData);
    }
});
