/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("videoForm");
    const videoFileInput = document.getElementById("videoFile");
    const tituloInput = document.getElementById("titulo");
    const fechaInput = document.getElementById("fecha");
    const duracionInput = document.getElementById("duracion");
    const formatoInput = document.getElementById("formato");

    // Función para obtener la fecha en formato "AAAA/MM/DD"
    function getFormattedDate() {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');
        return `${year}/${month}/${day}`;
    }

    // Establecer la fecha de creación automáticamente
    fechaInput.value = getFormattedDate();

    videoFileInput.addEventListener("change", function (event) {
        const file = event.target.files[0];

        if (file) {
            const video = document.createElement("video");
            video.preload = "metadata";

            video.onloadedmetadata = function () {
                window.URL.revokeObjectURL(video.src);

                // Obtener metadatos
                tituloInput.value = file.name.replace(/\.[^/.]+$/, ""); // Nombre del archivo sin extensión
                formatoInput.value = file.type.split("/")[1].toUpperCase(); // Formato del video en mayúsculas

                // Convertir la duración a formato HH:MM:SS
                const totalSeconds = Math.floor(video.duration);
                const hours = String(Math.floor(totalSeconds / 3600)).padStart(2, '0');
                const minutes = String(Math.floor((totalSeconds % 3600) / 60)).padStart(2, '0');
                const seconds = String(totalSeconds % 60).padStart(2, '0');
                duracionInput.value = `${hours}:${minutes}:${seconds}`;
            };

            video.src = URL.createObjectURL(file);
        }
    });

    form.addEventListener("submit", function (event) {
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        form.classList.add("was-validated");
    });
});
