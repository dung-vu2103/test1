function changeLang() {
            
            var valueLang = jQuery('.lang').value;
            if (valueLang == "en") {
                jQuery.getScript("/v3/js/lang/lang.en.js");
            } else if (valueLang == "vi") {
            	jQuery.getScript("/v3/js/lang/lang.vi.js");
            };
}