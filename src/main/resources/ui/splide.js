import Splide from '@splidejs/splide';
import '@splidejs/splide/dist/css/splide.min.css'

document.addEventListener( 'DOMContentLoaded', function() {
    var elms = document.getElementsByClassName( 'splide' );

    for (var i = 0; i < elms.length; i++) {
        new Splide(elms[i], {
            type   : 'loop',
            perPage: 3,
            perMove: 1,
        }).mount();
    }
});