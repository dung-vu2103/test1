let fileInput = document.getElementById("file");
let fileInput1 = document.getElementById("file1");
let image = document.getElementById("image");
let aspectRatio = document.querySelectorAll(".aspect-ratio-button");
const previewImage = document.getElementById("preview-image");
const previewImg = document.getElementById("previewImage");
const applyButton = document.getElementById("apply");
let cropper = "";
let fileName = "";
const closePopupButton = document.getElementById("closePopupButton");
const popupContainer = document.getElementById("popupContainer");
const dndElement = document.querySelector('#dnd');
const validImageTypes = ['image/gif', 'image/jpeg', 'image/png']

dndElement.addEventListener('dragover', function(e) {
    e.preventDefault()
    this.classList.add('drag-over')
})

dndElement.addEventListener('dragleave', function(e) {
    e.preventDefault()
    this.classList.remove('drag-over')
})

dndElement.addEventListener('drop', function(e) {
    e.preventDefault()
    const files = e.dataTransfer.files;
    for (var i = 0; i < files.length; i++) {
        const file = files[i]
        console.log(111, file)
    }
})

dndElement.addEventListener('drop', function(e) {
    e.preventDefault()
    const files = e.dataTransfer.files;
    for (var i = 0; i < files.length; i++) {
        const file = files[i]
        renderPreviewImage(file)
    }
})

function renderPreviewImage(file) {
    const fileType = file['type']

    if (!validImageTypes.includes(fileType)) {
        resultElement.insertAdjacentHTML(
            'beforeend',
            '<span class="preview-img">Chọn ảnh đi :3</span>'
        )
        return
    }

    let  reader = new FileReader();
    // console.log(reader)
    reader.readAsDataURL(file);

    reader.onload = () => {
        image.setAttribute("src", reader.result);
        if (cropper) {
            cropper.destroy();
        }
        cropper = new Cropper(image, {
            aspectRatio: 16 / 9,
        });

        // Thêm điều kiện để chỉ mở popup khi đã tải lên ảnh
        if (file.size > 0) {
            popupContainer.style.display = "block";
        }
    };
    fileName = file.name.split(".")[0];
}

closePopupButton.addEventListener("click", () => {
    image.src = "#";
    fileInput.value = "";
    popupContainer.style.display = "none";
});

fileInput.onchange = () => {
    let reader = new FileReader();
    console.log(reader)
    reader.readAsDataURL(fileInput.files[0]);

    reader.onload = () => {
        image.setAttribute("src", reader.result);
        if (cropper) {
            cropper.destroy();
        }
        cropper = new Cropper(image, {
            aspectRatio: 16 / 9,
        });

        // Thêm điều kiện để chỉ mở popup khi đã tải lên ảnh
        if (fileInput.files.length > 0) {
            popupContainer.style.display = "block";
        }
    };
    fileName = fileInput.files[0].name.split(".")[0];
};

//Set aspect ration
aspectRatio.forEach((element) => {
    element.addEventListener("click", () => {
        if (element.innerText == "Free") {
            cropper.setAspectRatio(NaN);
        } else {
            cropper.setAspectRatio(eval(element.innerText.replace(":", "/")));
        }
    });
});

applyButton.addEventListener("click", (e) => {
    e.preventDefault();
    let imgSrc = cropper.getCroppedCanvas({}).toDataURL("image/jpeg", 0.75);
    //Set previewImgOutPopup
    previewImg.src = imgSrc;
    fileInput1.value = imgSrc;

    $('img').closest("img").show();
    $('h4[id="chooseImg"]').closest("h4").hide();
    $('button[id="icon-button-edit"]').closest("button").show();
    $('button[id="icon-button-delete"]').closest("button").show();
    document.getElementById("popupContainer").style.display = "none";
});

window.onload = () => {
};