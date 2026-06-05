PRAGMA foreign_keys = ON;

-- empresas (única fila)
CREATE TABLE IF NOT EXISTS empresas (
    cod_emp         INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre          VARCHAR(100) NOT NULL,
    direccion       VARCHAR(200),
    telefono        VARCHAR(20),
    email           VARCHAR(100),
    director_general VARCHAR(100),
    jefe_rrhh       VARCHAR(100),
    jefe_contabilidad VARCHAR(100),
    secretario_sindicato VARCHAR(100)
);

-- proveedores
CREATE TABLE IF NOT EXISTS proveedores (
    id_proveedor    INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre          VARCHAR(100) NOT NULL,
    tipo_servicio   VARCHAR(50) NOT NULL,
    direccion       VARCHAR(200),
    telefono        VARCHAR(20),
    email           VARCHAR(100) UNIQUE,
    responsable     VARCHAR(100)
);

-- clientes
CREATE TABLE IF NOT EXISTS clientes (
    id_cliente      INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre          VARCHAR(50) NOT NULL,
    apellidos       VARCHAR(100) NOT NULL,
    num_documento   VARCHAR(20) UNIQUE NOT NULL,
    direccion       VARCHAR(200),
    telefono        VARCHAR(20),
    email           VARCHAR(100),
    trato_pref      BOOLEAN DEFAULT FALSE
);

-- empleados
CREATE TABLE IF NOT EXISTS empleados (
    id_empleado     INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre          VARCHAR(50) NOT NULL,
    apellidos       VARCHAR(100) NOT NULL,
    num_documento   VARCHAR(20) UNIQUE NOT NULL,
    direccion       VARCHAR(200),
    telefono        VARCHAR(20),
    email           VARCHAR(100) UNIQUE,
    cargo           VARCHAR(100),
    departamento    VARCHAR(100)
);

-- categorias_servicios_productos
CREATE TABLE IF NOT EXISTS categorias_servicios_productos (
    id_categoria    INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre          VARCHAR(50) NOT NULL UNIQUE
);

-- servicios_productos
CREATE TABLE IF NOT EXISTS servicios_productos (
    cod_servicio    VARCHAR(20) PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    descripcion     TEXT,
    id_categoria    INTEGER NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    es_tercero      BOOLEAN DEFAULT FALSE,
    estado_activo   BOOLEAN NOT NULL DEFAULT TRUE,
    id_proveedor    INTEGER NULL,
    FOREIGN KEY (id_categoria) REFERENCES categorias_servicios_productos(id_categoria),
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor) ON DELETE SET NULL
);

-- eventos
CREATE TABLE IF NOT EXISTS eventos (
    id_evento       INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_evento   VARCHAR(100) NOT NULL,
    tipo_evento     VARCHAR(50) NOT NULL,
    fecha_hora_ini  TIMESTAMP NOT NULL,
    fecha_hora_fin  TIMESTAMP NOT NULL,
    ubicacion       VARCHAR(200),
    num_invitados   INTEGER,
    id_cliente      INTEGER NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
);

-- contratos (relación 1:1 con evento)
CREATE TABLE IF NOT EXISTS contratos (
    num_contrato    VARCHAR(20) PRIMARY KEY,
    fecha_contrato  DATE NOT NULL,
    terminos_cond   TEXT,
    id_cliente      INTEGER NOT NULL,
    id_evento       INTEGER UNIQUE NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente),
    FOREIGN KEY (id_evento) REFERENCES eventos(id_evento) ON DELETE CASCADE
);

-- eventos_servicios (M:N entre evento y servicio_producto)
CREATE TABLE IF NOT EXISTS eventos_servicios (
    id_evento       INTEGER NOT NULL,
    cod_servicio    VARCHAR(20) NOT NULL,
    cantidad        INTEGER NOT NULL CHECK (cantidad > 0),
    PRIMARY KEY (id_evento, cod_servicio),
    FOREIGN KEY (id_evento) REFERENCES eventos(id_evento) ON DELETE CASCADE,
    FOREIGN KEY (cod_servicio) REFERENCES servicios_productos(cod_servicio)
);

-- eventos_empleados (M:N con responsabilidades)
CREATE TABLE IF NOT EXISTS eventos_empleados (
    id_evento       INTEGER NOT NULL,
    id_empleado     INTEGER NOT NULL,
    responsabilidades TEXT,
    PRIMARY KEY (id_evento, id_empleado),
    FOREIGN KEY (id_evento) REFERENCES eventos(id_evento) ON DELETE CASCADE,
    FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado)
);

-- contratos_lineas (servicios/productos contratados en un contrato)
CREATE TABLE IF NOT EXISTS contratos_lineas (
    num_contrato    VARCHAR(20) NOT NULL,
    cod_servicio    VARCHAR(20) NOT NULL,
    cantidad        INTEGER NOT NULL CHECK (cantidad > 0),
    precio_negociado DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (num_contrato, cod_servicio),
    FOREIGN KEY (num_contrato) REFERENCES contratos(num_contrato) ON DELETE CASCADE,
    FOREIGN KEY (cod_servicio) REFERENCES servicios_productos(cod_servicio)
);

-- pagos (cronograma de pagos)
CREATE TABLE IF NOT EXISTS pagos (
    id_pago         INTEGER PRIMARY KEY AUTOINCREMENT,
    num_contrato    VARCHAR(20) NOT NULL,
    fecha_pago      DATE NOT NULL,
    monto           DECIMAL(10,2) NOT NULL CHECK (monto > 0),
    FOREIGN KEY (num_contrato) REFERENCES contratos(num_contrato) ON DELETE CASCADE
);

-- modificaciones_contratos (historial de cambios)
CREATE TABLE IF NOT EXISTS modificaciones_contratos (
    id_mod          INTEGER PRIMARY KEY AUTOINCREMENT,
    num_contrato    VARCHAR(20) NOT NULL,
    fecha_mod       DATE NOT NULL,
    descripcion     TEXT NOT NULL,
    FOREIGN KEY (num_contrato) REFERENCES contratos(num_contrato) ON DELETE CASCADE
);

-- eventos_temas (múltiples temas por evento)
CREATE TABLE IF NOT EXISTS eventos_temas (
    id_evento       INTEGER NOT NULL,
    tema            VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_evento, tema),
    FOREIGN KEY (id_evento) REFERENCES eventos(id_evento) ON DELETE CASCADE
);

-- índices
CREATE INDEX IF NOT EXISTS idx_eventos_id_cliente ON eventos(id_cliente);
CREATE INDEX IF NOT EXISTS idx_contratos_id_cliente ON contratos(id_cliente);
CREATE INDEX IF NOT EXISTS idx_contratos_id_evento ON contratos(id_evento);
CREATE INDEX IF NOT EXISTS idx_eventos_servicios_cod_servicio ON eventos_servicios(cod_servicio);
CREATE INDEX IF NOT EXISTS idx_eventos_empleados_id_empleado ON eventos_empleados(id_empleado);
CREATE INDEX IF NOT EXISTS idx_contratos_lineas_cod_servicio ON contratos_lineas(cod_servicio);
CREATE INDEX IF NOT EXISTS idx_pagos_num_contrato ON pagos(num_contrato);
CREATE INDEX IF NOT EXISTS idx_modificaciones_num_contrato ON modificaciones_contratos(num_contrato);
CREATE INDEX IF NOT EXISTS idx_eventos_temas_id_evento ON eventos_temas(id_evento);
CREATE INDEX IF NOT EXISTS idx_servicios_productos_id_proveedor ON servicios_productos(id_proveedor);
CREATE INDEX IF NOT EXISTS idx_servicios_productos_id_categoria ON servicios_productos(id_categoria);