# Proyecto: Sistema de Gestión para una Empresa de Decoración de Fiestas

Existe una empresa dedicada a la planificación, diseño y ejecución de decoraciones para eventos y fiestas. De la misma se conoce su nombre, código, dirección postal, teléfono, nombre del director general, nombre del jefe del departamento de recursos humanos, nombre del responsable del departamento de contabilidad, nombre del secretario general del sindicato, y el logotipo que la identifica, consistente en una imagen.

## Información de la Empresa y sus Relaciones Comerciales

Esta empresa mantiene relaciones comerciales con diversos proveedores de servicios y productos para eventos, como floristerías, empresas de alquiler de mobiliario, compañías de catering y servicios de entretenimiento. De cada proveedor se conoce su nombre, tipo de servicio (floristería, mobiliario, catering, entretenimiento), dirección, teléfono, email de contacto y nombre del responsable.

## Información de los Eventos

Cada evento gestionado por la empresa se registra con detalles específicos, incluyendo el nombre del evento, tipo de evento (boda, cumpleaños, corporativo, etc.), fecha y hora de inicio y fin, ubicación del evento, número de invitados, y temas o conceptos de decoración solicitados.

## Información de los Clientes

De cada cliente se conoce su nombre, apellidos, número de documento de identidad, dirección, teléfono, email y detalles de eventos anteriores realizados con la empresa. Se registra si el cliente tiene un trato preferencial, lo cual repercute en descuentos y servicios adicionales.

## Información de los Servicios y Productos

Cada servicio o producto ofrecido por la empresa tiene un código, nombre, descripción, categoría (floral, mobiliario, iluminación, entretenimiento, catering, etc.), precio unitario, y si se trata de un servicio de terceros o propio.

## Información de los Empleados

Cada empleado de la empresa se registra con su nombre, apellidos, número de documento de identidad, dirección, teléfono, email, cargo, departamento y si tiene responsabilidades específicas en eventos.

## Información de los Contratos

Cada contrato para un evento incluye información detallada sobre el cliente, los servicios y productos contratados, los precios negociados, el cronograma de pagos, y cualquier término y condición especial acordada. Los contratos también registran cualquier modificación o actualización realizada.

## Requerimientos Funcionales

El sistema debe permitir gestionar de forma eficiente toda la información almacenada, asegurando la integridad de los datos y evitando duplicaciones innecesarias. Los requerimientos específicos incluyen:

- **Gestión de Eventos**: Crear, modificar y eliminar eventos, asegurando que se mantenga un registro detallado de cada uno, incluyendo los proveedores y servicios asignados.
- **Gestión de Clientes**: Insertar, modificar y eliminar la información de los clientes, asegurando que no haya duplicados en el sistema.
- **Gestión de Proveedores**: Registrar y consultar proveedores, incluyendo detalles de los servicios y productos que ofrecen.
- **Gestión de Empleados**: Registrar y consultar empleados, vinculándolos a eventos y tareas específicas.
- **Gestión de Servicios y Productos**: Registrar y consultar servicios y productos, asegurando que se actualicen los precios y las descripciones según sea necesario.
- **Gestión de Contratos**: Crear y modificar contratos, manteniendo un registro detallado de los términos y condiciones acordados, así como cualquier cambio realizado.

## Reportes

El sistema debe ofrecer los siguientes reportes:

### 1. Ficha de la Empresa

- Nombre de la empresa
- Dirección postal de la empresa
- Logotipo de la entidad
- Teléfono e email de contacto
- Nombre del director general
- Nombre del jefe del departamento de recursos humanos
- Nombre del jefe del departamento de contabilidad
- Nombre del secretario general del sindicato

### 2. Ficha de un Cliente Determinado

- Nombre del cliente
- Número de documento de identidad
- Dirección y teléfono
- Email de contacto
- Detalles de eventos anteriores
- Si el cliente tiene trato preferencial
- Información de contacto
- Tarifas y descuentos aplicables

### 3. Ficha de un Proveedor Determinado

- Nombre del proveedor
- Tipo de servicio
- Dirección
- Teléfono y email de contacto
- Nombre del responsable

### 4. Reporte de Eventos Realizados en un Periodo de Tiempo

- Nombre del evento
- Tipo de evento
- Fecha y hora de inicio
- Fecha y hora de fin
- Ubicación
- Número de invitados
- Temas o conceptos de decoración
- **Nota:** Presentar la información ordenada por fecha de inicio del evento.

### 5. Reporte de Servicios y Productos Utilizados en un Evento Específico

- Nombre del evento
- Código del servicio o producto
- Nombre del servicio o producto
- Descripción

### 6. Reporte de Empleados Asignados a un Evento

- Nombre del evento
- Nombre del empleado
- Cargo
- Departamento
- Responsabilidades específicas
- **Nota:** Presentar la información ordenada por cargo y nombre del empleado.

### 7. Reporte de Contratos por Cliente en un Período de Tiempo

- Nombre del cliente
- Número de contrato
- Detalles del evento
- Servicios y productos contratados
- Precios negociados
- Cronograma de pagos
- Términos y condiciones
- **Nota:** Presentar la información ordenada por fecha de contrato y nombre del cliente.

### 8. Reporte de Estado de los Proveedores

- Nombre del proveedor
- Tipo de servicio
- Estado de los servicios (activo, inactivo)
- **Nota:** Presentar la información ordenada por tipo de servicio y nombre del proveedor.

### 9. Reporte Consolidado de los Servicios y Productos en un Año Determinado

- Año del cual se va a mostrar la información
- Categoría de servicio o producto
- Cantidad utilizada
- Monto total facturado
- **Nota:** Presentar la información ordenada por categoría y año.

### 10. Reporte de Modificaciones en Contratos en un Período de Tiempo

- Número de contrato
- Nombre del cliente
- Detalles del evento
- Modificaciones realizadas
- Fecha de modificación
- **Nota:** Presentar la información ordenada por fecha de modificación y número de contrato.

## Diseño de la Base de Datos

El diseño físico de la base de datos debe considerar todos los elementos que optimicen el funcionamiento del sistema, tales como disparadores, restricciones, índices de búsqueda, además de las llaves primarias y extranjeras, que permitan trabajar con la integridad requerida.

---

> Este archivo se generó basado en el PDF proporcionado por el profesor.