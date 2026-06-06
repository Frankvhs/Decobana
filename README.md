# Decobana

Sistema de base de datos para una empresa de decoración de fiestas y eventos. Proyecto final de Base de Datos para [**Segundo año de ingeniería informática de la CUJAE**](https://moodle.cujae.edu.cu/course/index.php?categoryid=49).

- **Empresa:** Decobana (ficticia)
- **Tecnología:** SQLite + Java Swing (vanilla JDBC)

## Requisitos

- Java JDK 11 o superior
- Eclipse IDE (recomendado) o cualquier IDE Java
- Maven (incluido en Eclipse, o instalado aparte si se compila por línea de comandos)

## Cómo ejecutar esta cosa?

### Opción 1: Desde Eclipse

1. **Importar el proyecto:**
   - `File → Import → Maven → Existing Maven Projects`
   - Seleccionar la carpeta raíz del proyecto (donde está `pom.xml`).
   - Eclipse descargará automáticamente el driver de SQLite.

2. **Ejecutar la aplicación:**
   - En el `Package Explorer`, navegar hasta `src/main/java/com/decobana/App.java`.
   - Click derecho → `Run As → Java Application`.
   - La base de datos `decobana.db` se creará automáticamente en la raíz del proyecto al iniciar.

3. **Generar JAR ejecutable (opcional):**
   - Click derecho sobre el proyecto → `Run As → Maven build...`.
   - En `Goals` escribir: `clean package`.
   - El JAR se generará en la carpeta `target/`.
   - Ejecutar con: `java -jar target/decobana-app-1.0.0.jar`

### Opción 2: Desde línea de comandos (Maven)

```bash
# 1. Clonar o copiar el proyecto
cd decobana

# 2. Compilar
mvn clean compile

# 3. Ejecutar
mvn exec:java -Dexec.mainClass="com.decobana.App"

# Alternativa: empaquetar y ejecutar el JAR
mvn clean package
java -jar target/decobana-app-1.0.0.jar
```

## Probar la base de datos manualmente

```bash
# Crear base de datos y ejecutar migración
sqlite3 decobana.db < src/main/resources/db/migration/initial_db.sql

# Verificar tablas creadas
sqlite3 decobana.db ".tables"

# Ver esquema completo
sqlite3 decobana.db ".schema"
```

## Entidades Principales

- Empresa (ficha única)
- Clientes
- Proveedores
- Eventos
- Servicios/Productos
- Empleados
- Contratos

## Reportes implementados

1. Ficha de la Empresa
2. Ficha de un Cliente Determinado
3. Ficha de un Proveedor Determinado
4. Reporte de Eventos Realizados en un Periodo de Tiempo
5. Reporte de Servicios y Productos Utilizados en un Evento Específico
6. Reporte de Empleados Asignados a un Evento
7. Reporte de Contratos por Cliente en un Período de Tiempo
8. Reporte de Estado de los Proveedores
9. Reporte Consolidado de los Servicios y Productos en un Año Determinado
10. Reporte de Modificaciones en Contratos en un Período de Tiempo

## Estado del Proyecto

- [x] Modelo de base de datos
- [x] Migración SQL inicial
- [x] Implementación Java (CRUD completo)
- [x] Interfaz gráfica con Swing
- [x] Los 10 reportes requeridos
- [ ] Correcciones de integridad transaccional (ver revisión)
- [ ] Gestión de logotipo de empresa
- [ ] Historial automático de modificaciones de contratos

---

_Proyecto para la CUJAE - Entrega final Base de Datos_
