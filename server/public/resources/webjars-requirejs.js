/*global requirejs */

// Ensure any request for this webjar brings in jQuery.
requirejs.config({
    shim: {
        'nv.d3': [ 'webjars!d3.js' ]
    }
});
