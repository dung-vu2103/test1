document.addEventListener("DOMContentLoaded", function () {
    const uploadForm = document.getElementById("form-update-live");
    const titleInput = document.getElementById("title");
    const descriptionInput = document.getElementById("description");
    const enableChat = document.getElementById("enable_chat");
    const privacy = document.getElementById("privacy");
    const timeEventStartInput = document.getElementById("time_event_start");
    const fileAvatar = document.getElementById("file1");
    const submitButton = document.getElementById("submitButton");
    const sucsessPopup = document.getElementById("sucsessPopup");

    uploadForm.addEventListener("submit", function (event) {
        event.preventDefault();
        uploadFile()
    });

    function openPopup() {
        sucsessPopup.style.display = "block";
        submitButton.style.display = "none";
    }

    function uploadFile() {
        const formData = new FormData();
        formData.append("title", titleInput.value);
        formData.append("description", descriptionInput.value);
        formData.append("enable_chat", enableChat.value);
        formData.append("privacy", privacy.value);
        formData.append("time_event_start", timeEventStartInput.value);
        formData.append("thumbUpload", fileAvatar.value);

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "", true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    openPopup();
                    const redirectUrl  = xhr.responseText;
                    if (redirectUrl) {
                        window.location.href = '/' + window.location.pathname.split('/')[1] + redirectUrl;
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
