document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("loginForm");
    const loginError = document.getElementById("loginError");

    // Redirigir si ya está logueado
    if (localStorage.getItem("adminLogueado")) {
        window.location.href = "dashboard.html";
    }

    loginForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const usuario = document.getElementById("usuario").value;
        const password = document.getElementById("password").value;

        try {
            const response = await fetch("http://localhost:8080/api/administradores/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ usuario, password })
            });

            const result = await response.json();

            // Ajusta "result.value" dependiendo de cómo tu ResultResponseMapper serializa la respuesta.
            // Si tu backend devuelve un status 200 para éxito:
            if (response.ok) {
                localStorage.setItem("adminLogueado", "true");
                window.location.href = "dashboard.html";
            } else {
                loginError.classList.remove("d-none");
            }
        } catch (error) {
            console.error("Error al conectar con el backend:", error);
            alert("Error de conexión con el servidor.");
        }
    });
});