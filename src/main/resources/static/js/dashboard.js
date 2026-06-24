document.addEventListener("DOMContentLoaded", () => {

    // 1. Protección de ruta
    if (!localStorage.getItem("adminLogueado")) {
        window.location.href = "index.html";
        return;
    }

    // 2. Cerrar sesión
    document.getElementById("btnLogout").addEventListener("click", () => {
        localStorage.removeItem("adminLogueado");
        window.location.href = "index.html";
    });

    // 3. Inicializar Mapa (Centrado en El Salvador)
    const map = L.map('mapaEmpleados').setView([13.7942, -88.8965], 8);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
    }).addTo(map);

    // 4. Cargar Empleados (Tabla y Marcadores)
    async function cargarEmpleados() {
        try {
            // URL ACTUALIZADA A RENDER
            const response = await fetch("https://backend-parcial2.onrender.com/api/empleados");
            const data = await response.json();

            const empleados = data.value || data;
            const tbody = document.querySelector("#tablaEmpleados tbody");
            tbody.innerHTML = "";

            if (!empleados || empleados.length === 0) {
                tbody.innerHTML = `<tr><td colspan="3" class="text-center text-muted">No hay empleados registrados</td></tr>`;
                return;
            }

            empleados.forEach(emp => {
                // Inyectamos los datos en la fila para usarlos al hacer clic
                tbody.innerHTML += `
                    <tr class="fila-empleado" style="cursor: pointer;" title="Clic para ver QR"
                        data-carnet="${emp.carnet}" data-nombre="${emp.nombre}" data-depto="${emp.departamento}">
                        <td><strong>${emp.carnet}</strong></td>
                        <td>${emp.nombre}</td>
                        <td>${emp.departamento}</td>
                    </tr>
                `;

                if (emp.latitud && emp.longitud) {
                    L.marker([parseFloat(emp.latitud), parseFloat(emp.longitud)])
                        .addTo(map)
                        .bindPopup(`<b>${emp.nombre}</b><br>${emp.departamento}`);
                }
            });
        } catch (error) {
            console.error("Error cargando empleados:", error);
            document.querySelector("#tablaEmpleados tbody").innerHTML =
                `<tr><td colspan="3" class="text-center text-danger">Error al cargar empleados</td></tr>`;
        }
    }

    // 5. Función para inyectar datos y mostrar el Modal del QR
    function mostrarQR(carnet, nombre, depto) {
        document.getElementById('qrEmpleadoNombre').textContent = nombre;
        document.getElementById('qrEmpleadoDepto').textContent = depto;
        document.getElementById('qrEmpleadoCarnet').textContent = carnet;

        // Generar QR usando API gratuita
        const qrUrl = `https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=${encodeURIComponent(carnet)}`;
        document.getElementById('qrImagen').src = qrUrl;

        // Instanciar y abrir el Modal de Bootstrap
        const qrModal = new bootstrap.Modal(document.getElementById('qrModal'));
        qrModal.show();
    }

    // Escuchar clics en la tabla de empleados
    document.querySelector("#tablaEmpleados tbody").addEventListener("click", (evento) => {
        const fila = evento.target.closest(".fila-empleado");
        if (fila) {
            const carnet = fila.getAttribute("data-carnet");
            const nombre = fila.getAttribute("data-nombre");
            const depto = fila.getAttribute("data-depto");
            mostrarQR(carnet, nombre, depto);
        }
    });

    // 6. Cargar Reporte Dinámico (Diario o Mensual)
    async function cargarReporte(tipoFiltro) {
        try {
            // URL ACTUALIZADA A RENDER
            const response = await fetch(`https://backend-parcial2.onrender.com/api/asistencias/reporte/${tipoFiltro}`);
            const data = await response.json();

            const reportes = data.value || data;
            const tbody = document.querySelector("#tablaReportes tbody");
            tbody.innerHTML = "";

            if (!reportes || reportes.length === 0 || data.success === false) {
                // Actualizado para abarcar 5 columnas (colspan="5")
                tbody.innerHTML = `<tr><td colspan="5" class="text-center text-muted">No hay datos para este período</td></tr>`;
                return;
            }

            reportes.forEach(rep => {
                // Dibujamos las 5 columnas, incluyendo rep.horaEntrada y rep.horaSalida
                tbody.innerHTML += `
                    <tr>
                        <td>${rep.carnet}</td>
                        <td>${rep.periodo}</td>
                        <td><span class="badge bg-secondary">${rep.horaEntrada}</span></td>
                        <td><span class="badge bg-secondary">${rep.horaSalida}</span></td>
                        <td><strong>${rep.horasTrabajadas} hrs</strong></td>
                    </tr>
                `;
            });
        } catch (error) {
            console.error(`Error cargando el reporte ${tipoFiltro}:`, error);
            document.querySelector("#tablaReportes tbody").innerHTML =
                `<tr><td colspan="5" class="text-center text-danger">Error al conectar con el servidor</td></tr>`;
        }
    }

    // Escuchar el cambio en el select del Reporte
    const filtroReporte = document.getElementById("filtroReporte");
    if (filtroReporte) {
        filtroReporte.addEventListener("change", (evento) => {
            cargarReporte(evento.target.value);
        });
    }

    // --- 7. HISTORIAL DE ASISTENCIAS Y FILTROS ---

    let historialCompleto = []; // Guarda todos los registros para filtrarlos en el navegador

    // Función que dibuja la tabla recibiendo un arreglo de datos
    function renderizarTablaAsistencias(datos) {
        const tbody = document.querySelector("#tablaAsistencias tbody");
        tbody.innerHTML = "";

        if (!datos || datos.length === 0) {
            tbody.innerHTML = `<tr><td colspan="4" class="text-center text-muted">No hay registros que coincidan</td></tr>`;
            return;
        }

        datos.forEach(asis => {
            const fecha = new Date(asis.fechaHora).toLocaleString('es-ES');
            tbody.innerHTML += `
                <tr>
                    <td>${asis.id}</td>
                    <td>${asis.empleado.carnet}</td>
                    <td>${asis.empleado.nombre}</td>
                    <td>${fecha}</td>
                </tr>
            `;
        });
    }

    // Función principal que trae los datos de Spring Boot
    async function cargarAsistencias() {
        try {
            // URL ACTUALIZADA A RENDER
            const response = await fetch("https://backend-parcial2.onrender.com/api/asistencias");
            const data = await response.json();

            historialCompleto = data.value || data;

            if (data.success === false) {
                historialCompleto = [];
            }

            renderizarTablaAsistencias(historialCompleto);

        } catch (error) {
            console.error("Error cargando asistencias:", error);
            document.querySelector("#tablaAsistencias tbody").innerHTML =
                `<tr><td colspan="4" class="text-center text-danger">Error al conectar con el servidor</td></tr>`;
        }
    }

    // Lógica del Botón "Filtrar"
    document.getElementById("btnFiltrar").addEventListener("click", () => {
        const textoFiltro = document.getElementById("filtroId").value.toLowerCase().trim();
        const fechaInicio = document.getElementById("filtroFechaInicio").value;
        const fechaFin = document.getElementById("filtroFechaFin").value;

        const datosFiltrados = historialCompleto.filter(asis => {
            // Filtrar por ID o Carnet
            const coincideTexto = asis.id.toString() === textoFiltro ||
                                  asis.empleado.carnet.toLowerCase().includes(textoFiltro) ||
                                  textoFiltro === "";

            // Filtrar por Rango de Fechas
            let coincideFecha = true;
            if (fechaInicio || fechaFin) {
                const fechaAsistencia = asis.fechaHora.split("T")[0];

                if (fechaInicio && fechaAsistencia < fechaInicio) coincideFecha = false;
                if (fechaFin && fechaAsistencia > fechaFin) coincideFecha = false;
            }

            return coincideTexto && coincideFecha;
        });

        renderizarTablaAsistencias(datosFiltrados);
    });

    // Lógica del Botón "Limpiar"
    document.getElementById("btnLimpiar").addEventListener("click", () => {
        document.getElementById("filtroId").value = "";
        document.getElementById("filtroFechaInicio").value = "";
        document.getElementById("filtroFechaFin").value = "";

        renderizarTablaAsistencias(historialCompleto);
    });

    // 8. Ejecutar todas las cargas iniciales al abrir la página
    cargarEmpleados();
    cargarReporte("diario");
    cargarAsistencias();
});