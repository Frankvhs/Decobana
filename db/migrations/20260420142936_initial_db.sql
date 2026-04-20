PRAGMA foreign_keys = ON;

-- EMPRESA
CREATE TABLE empresa (
    id INTEGER PRIMARY KEY CHECK (id = 1),
    nombre TEXT NOT NULL,
    codigo TEXT,
    direccion_postal TEXT,
    telefono TEXT,
    director_general TEXT,
    jefe_rrhh TEXT,
    responsable_contabilidad TEXT,
    secretario_sindicato TEXT,
    logotipo BLOB
);

-- PROVEEDORES
CREATE TABLE proveedores (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    tipo_servicio TEXT NOT NULL CHECK (tipo_servicio IN ('floristeria','mobiliario','catering','entretenimiento')),
    direccion TEXT,
    telefono TEXT,
    email_contacto TEXT,
    nombre_responsable TEXT
);
CREATE INDEX idx_proveedores_tipo_servicio ON proveedores(tipo_servicio);

-- CLIENTES
CREATE TABLE clientes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    apellidos TEXT NOT NULL,
    numero_documento TEXT UNIQUE NOT NULL,
    direccion TEXT,
    telefono TEXT,
    email TEXT,
    detalles_eventos_anteriores TEXT,
    trato_preferencial BOOLEAN DEFAULT 0 CHECK (trato_preferencial IN (0,1))
);
CREATE INDEX idx_clientes_numero_documento ON clientes(numero_documento);
CREATE INDEX idx_clientes_nombre_apellidos ON clientes(nombre, apellidos);

-- EVENTOS
CREATE TABLE eventos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_evento TEXT NOT NULL,
    tipo_evento TEXT NOT NULL CHECK (tipo_evento IN ('boda','cumpleaños','corporativo','otro')),
    fecha_hora_inicio DATETIME NOT NULL,
    fecha_hora_fin DATETIME NOT NULL,
    ubicacion TEXT,
    numero_invitados INTEGER,
    temas_decoracion TEXT
);
CREATE INDEX idx_eventos_fecha_inicio ON eventos(fecha_hora_inicio);

-- SERVICIOS_PRODUCTOS
CREATE TABLE servicios_productos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    codigo TEXT UNIQUE NOT NULL,
    nombre TEXT NOT NULL,
    descripcion TEXT,
    categoria TEXT NOT NULL CHECK (categoria IN ('floral','mobiliario','iluminacion','entretenimiento','catering','otro')),
    precio_unitario REAL NOT NULL CHECK (precio_unitario >= 0),
    es_terceros BOOLEAN DEFAULT 0 CHECK (es_terceros IN (0,1)),
    proveedor_id INTEGER,
    FOREIGN KEY (proveedor_id) REFERENCES proveedores(id) ON DELETE SET NULL
);
CREATE INDEX idx_servicios_codigo ON servicios_productos(codigo);
CREATE INDEX idx_servicios_proveedor ON servicios_productos(proveedor_id);
CREATE INDEX idx_servicios_categoria ON servicios_productos(categoria);

-- EMPLEADOS
CREATE TABLE empleados (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    apellidos TEXT NOT NULL,
    numero_documento TEXT UNIQUE NOT NULL,
    direccion TEXT,
    telefono TEXT,
    email TEXT,
    cargo TEXT,
    departamento TEXT,
    responsabilidades_evento TEXT
);
CREATE INDEX idx_empleados_numero_documento ON empleados(numero_documento);

-- CONTRATOS
CREATE TABLE contratos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    numero_contrato TEXT UNIQUE NOT NULL,
    cliente_id INTEGER NOT NULL,
    evento_id INTEGER NOT NULL,
    fecha_contrato DATE NOT NULL,
    precio_total_negociado REAL CHECK (precio_total_negociado >= 0),
    cronograma_pagos TEXT,
    terminos_condiciones TEXT,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE RESTRICT,
    FOREIGN KEY (evento_id) REFERENCES eventos(id) ON DELETE RESTRICT
);
CREATE INDEX idx_contratos_cliente ON contratos(cliente_id);
CREATE INDEX idx_contratos_evento ON contratos(evento_id);
CREATE INDEX idx_contratos_fecha ON contratos(fecha_contrato);
CREATE INDEX idx_contratos_numero ON contratos(numero_contrato);

-- CONTRATO_SERVICIOS (detalle de servicios/productos por contrato)
CREATE TABLE contrato_servicios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    contrato_id INTEGER NOT NULL,
    servicio_producto_id INTEGER NOT NULL,
    cantidad INTEGER NOT NULL DEFAULT 1 CHECK (cantidad > 0),
    precio_negociado REAL NOT NULL CHECK (precio_negociado >= 0),
    FOREIGN KEY (contrato_id) REFERENCES contratos(id) ON DELETE CASCADE,
    FOREIGN KEY (servicio_producto_id) REFERENCES servicios_productos(id) ON DELETE RESTRICT,
    UNIQUE(contrato_id, servicio_producto_id)
);
CREATE INDEX idx_contrato_servicios_contrato ON contrato_servicios(contrato_id);

-- EMPLEADO_EVENTO (asignacion de empleados a eventos)
CREATE TABLE empleado_evento (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    empleado_id INTEGER NOT NULL,
    evento_id INTEGER NOT NULL,
    responsabilidad_especifica TEXT,
    FOREIGN KEY (empleado_id) REFERENCES empleados(id) ON DELETE CASCADE,
    FOREIGN KEY (evento_id) REFERENCES eventos(id) ON DELETE CASCADE,
    UNIQUE(empleado_id, evento_id)
);
CREATE INDEX idx_empleado_evento_evento ON empleado_evento(evento_id);
CREATE INDEX idx_empleado_evento_empleado ON empleado_evento(empleado_id);

-- HISTORIAL_CONTRATO (modificaciones de contratos)
CREATE TABLE historial_contrato (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    contrato_id INTEGER NOT NULL,
    fecha_modificacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    descripcion_modificacion TEXT NOT NULL,
    FOREIGN KEY (contrato_id) REFERENCES contratos(id) ON DELETE CASCADE
);
CREATE INDEX idx_historial_contrato_fecha ON historial_contrato(fecha_modificacion);
CREATE INDEX idx_historial_contrato_contrato ON historial_contrato(contrato_id);

-- PAGOS_PLAN
CREATE TABLE pagos_plan (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    contrato_id INTEGER NOT NULL,
    fecha_programada DATE NOT NULL,
    monto REAL NOT NULL CHECK (monto >= 0),
    estado TEXT NOT NULL DEFAULT 'pendiente' CHECK (estado IN ('pendiente','pagado','vencido')),
    FOREIGN KEY (contrato_id) REFERENCES contratos(id) ON DELETE CASCADE
);
CREATE INDEX idx_pagos_plan_contrato ON pagos_plan(contrato_id);
CREATE INDEX idx_pagos_plan_fecha ON pagos_plan(fecha_programada);
