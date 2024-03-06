let fileInput_ = document.getElementById("file_");
let fileInput1_ = document.getElementById("file1_");
let image1 = document.getElementById("image1");
let downloadButton1 = document.getElementById("download1");
let aspectRatio1 = document.querySelectorAll(".aspect-ratio-button");
const previewButton1 = document.getElementById("preview");
const previewImage1 = document.getElementById("preview-image");
const previewImg1 = document.getElementById("previewImage1");
const applyButton1 = document.getElementById("apply1");
const options1 = document.querySelector(".options");
let cropper1 = "";
let fileName1 = "";
const closePopupButton1 = document.getElementById("closePopupButton1");
const popupContainer1 = document.getElementById("popupContainer1");
const popupValidateImg1 = document.getElementById("popupValidateImg1");
// const editCropper1 = document.getElementById("editCropper1");
// const deleteCropper1 = document.getElementById("deleteCropper1");
const containerFluid1 = document.getElementById("container-fluid1");
const dndElement1 = document.querySelector('#dnd1');
const validImageTypes1 = ['image/gif', 'image/jpeg', 'image/png']



dndElement1.addEventListener('dragover', function(e) {
    e.preventDefault()
    this.classList.add('drag-over')
})

dndElement1.addEventListener('dragleave', function(e) {
    e.preventDefault()
    this.classList.remove('drag-over')
})

dndElement1.addEventListener('drop', function(e) {
    e.preventDefault()
    const files = e.dataTransfer.files;
    for (var i = 0; i < files.length; i++) {
        const file = files[i]
        console.log(111, file)
    }
})

dndElement1.addEventListener('drop', function(e) {
    e.preventDefault()
    const files = e.dataTransfer.files;
    for (var i = 0; i < files.length; i++) {
        const file1 = files[i]
        renderPreviewImage1(file1)
    }
})

function renderPreviewImage1(file1) {
    const fileType = file1['type']

    if (!validImageTypes1.includes(fileType)) {
        resultElement.insertAdjacentHTML(
            'beforeend',
            '<span class="preview-img">Chọn ảnh đi :3</span>'
        )
        return
    }

    let  reader = new FileReader();
    // console.log(reader)
    reader.readAsDataURL(file1);

    reader.onload = () => {
        image1.setAttribute("src", reader.result);
        if (cropper1) {
            cropper1.destroy();
        }
        cropper1 = new Cropper(image1, {
            aspectRatio: 16 / 9,
        });

        // Thêm điều kiện để chỉ mở popup khi đã tải lên ảnh
        if (file1.size > 0) {
            popupContainer1.style.display = "block";
        }
    };
    fileName1 = file.name.split(".")[0];
}

closePopupButton1.addEventListener("click", () => {
    fileInput_.value = "";
    image1.src = "#";
    popupContainer1.style.display = "none";
});

fileInput_.onchange = () => {
    let reader = new FileReader();
    console.log(reader)
    reader.readAsDataURL(fileInput_.files[0]);

    reader.onload = () => {
        image1.setAttribute("src", reader.result);
        if (cropper1) {
            cropper1.destroy();
        }
        cropper1 = new Cropper(image1, {
            aspectRatio: 16 / 9,
        });

        // Thêm điều kiện để chỉ mở popup khi đã tải lên ảnh
        if (fileInput_.files.length > 0) {
            popupContainer1.style.display = "block";
        }
    };
    fileName1 = fileInput_.files[0].name.split(".")[0];
};

//Set aspect ration
aspectRatio1.forEach((element) => {
    element.addEventListener("click", () => {
        if (element.innerText == "Free") {
            cropper1.setAspectRatio(NaN);
        } else {
            cropper1.setAspectRatio(eval(element.innerText.replace(":", "/")));
        }
    });
});

applyButton1.addEventListener("click", (e) => {
    e.preventDefault();
    let imgSrc = cropper1.getCroppedCanvas({}).toDataURL("image/jpeg", 0.75);
    //Set previewImgOutPopup
    previewImg1.src = imgSrc;
    fileInput1_.value = imgSrc;

    $('img[id="previewImage1"]').closest("img").show();
    $('h4[id="chooseImg1"]').closest("h4").hide();
    $('button[id="icon-button-edit1"]').closest("button").show();
    $('button[id="icon-button-delete1"]').closest("button").show();
    document.getElementById("popupContainer1").style.display = "none";
});

// editCropper1.addEventListener("click", (e) => {
//     document.getElementById("popupContainer1").style.display = "block";
// });
//
// deleteCropper1.addEventListener("click", (e) => {
//     previewImg1.src = "#";
//     fileInput_.src = "#";
//     image1.src = "#";
//
//     $('div').each(function() {
//         if($(this).find('img[id="previewImage1"]').attr('src') == "#"){
//             $('img[src="#"]').closest("img").hide();
//             $('button[id="icon-button-edit1"]').closest("button").hide();
//             $('button[id="icon-button-delete1"]').closest("button").hide();
//             $('h4[id="chooseImg1"]').closest("h4").show();
//         }
//     });
// });

window.onload = () => {
    options1.classList.add("hide");
};