window.onload = () => {
    $(document).on("change", ".video_here", function(evt) {
        var $source = $('#here');
        console.log($source);
        $source[0].src = URL.createObjectURL(this.files[0]);
        $source.parent()[0].load();
        $('video').closest("div").show();
        $('h4[id="chooseVid"]').closest("h4").hide();
    });
};