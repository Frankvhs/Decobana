
-- empresas (única fila)
CREATE TABLE empresas (
    cod_emp         SERIAL PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    direccion       VARCHAR(200),
    telefono        VARCHAR(20),
    email           VARCHAR(100),
    logotipo        TEXT, -- pensado para que sea una ruta
    director_general VARCHAR(100),
    jefe_rrhh       VARCHAR(100),
    jefe_contabilidad VARCHAR(100),
    secretario_sindicato VARCHAR(100)
);

-- 
CREATE TABLE proveedores (
    id_proveedor    SERIAL PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    tipo_servicio   VARCHAR(50) NOT NULL,
    direccion       VARCHAR(200),
    telefono        VARCHAR(20),
    email           VARCHAR(100) UNIQUE,
    responsable     VARCHAR(100)
);

-- 
CREATE TABLE clientes (
    id_cliente      SERIAL PRIMARY KEY,
    nombre          VARCHAR(50) NOT NULL,
    apellidos       VARCHAR(100) NOT NULL,
    num_documento   VARCHAR(20) UNIQUE NOT NULL,
    direccion       VARCHAR(200),
    telefono        VARCHAR(20),
    email           VARCHAR(100),
    trato_pref      BOOLEAN DEFAULT FALSE
);

-- 
CREATE TABLE empleados (
    id_empleado     SERIAL PRIMARY KEY,
    nombre          VARCHAR(50) NOT NULL,
    apellidos       VARCHAR(100) NOT NULL,
    cargo           VARCHAR(100),
    departamento    VARCHAR(100)
);

-- 
CREATE TABLE servicios_productos (
    cod_servicio    VARCHAR(20) PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    descripcion     TEXT,
    categoria       VARCHAR(50) NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    es_tercero      BOOLEAN DEFAULT FALSE,
    id_proveedor    INTEGER NULL,
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor) ON DELETE SET NULL
);

-- 
CREATE TABLE eventos (
    id_evento       SERIAL PRIMARY KEY,
    nombre_evento   VARCHAR(100) NOT NULL,
    tipo_evento     VARCHAR(50) NOT NULL,
    fecha_hora_ini  TIMESTAMP NOT NULL,
    fecha_hora_fin  TIMESTAMP NOT NULL,
    ubicacion       VARCHAR(200),
    num_invitados   INTEGER,
    id_cliente      INTEGER NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) ON DELETE RESTRICT
);

-- (relación 1:1 con evento, pero separada)
CREATE TABLE contratos (
    num_contrato    VARCHAR(20) PRIMARY KEY,
    fecha_contrato  DATE NOT NULL,
    terminos_cond   TEXT,
    id_cliente      INTEGER NOT NULL,
    id_evento       INTEGER UNIQUE NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) ON DELETE RESTRICT,
    FOREIGN KEY (id_evento) REFERENCES eventos(id_evento) ON DELETE CASCADE
);

-- (relación M:N entre evento y servicio_producto)
CREATE TABLE eventos_servicios (
    id_evento       INTEGER NOT NULL,
    cod_servicio    VARCHAR(20) NOT NULL,
    cantidad        INTEGER NOT NULL CHECK (cantidad > 0),
    PRIMARY KEY (id_evento, cod_servicio),
    FOREIGN KEY (id_evento) REFERENCES eventos(id_evento) ON DELETE CASCADE,
    FOREIGN KEY (cod_servicio) REFERENCES servicios_productos(cod_servicio) ON DELETE RESTRICT
);

-- (relación M:N con responsabilidades)
CREATE TABLE eventos_empleados (
    id_evento       INTEGER NOT NULL,
    id_empleado     INTEGER NOT NULL,
    responsabilidades TEXT,
    PRIMARY KEY (id_evento, id_empleado),
    FOREIGN KEY (id_evento) REFERENCES eventos(id_evento) ON DELETE CASCADE,
    FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado) ON DELETE RESTRICT
);

-- (servicios/productos contratados en un contrato)
CREATE TABLE contratos_lineas (
    num_contrato    VARCHAR(20) NOT NULL,
    cod_servicio    VARCHAR(20) NOT NULL,
    cantidad        INTEGER NOT NULL CHECK (cantidad > 0),
    precio_negociado DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (num_contrato, cod_servicio),
    FOREIGN KEY (num_contrato) REFERENCES contratos(num_contrato) ON DELETE CASCADE,
    FOREIGN KEY (cod_servicio) REFERENCES servicios_productos(cod_servicio) ON DELETE RESTRICT
);

-- (cronograma de pagos de un contrato)
CREATE TABLE pagos (
    id_pago         SERIAL PRIMARY KEY,
    num_contrato    VARCHAR(20) NOT NULL,
    fecha_pago      DATE NOT NULL,
    monto           DECIMAL(10,2) NOT NULL CHECK (monto > 0),
    FOREIGN KEY (num_contrato) REFERENCES contratos(num_contrato) ON DELETE CASCADE
);

-- (historial de cambios en contratos)
CREATE TABLE modificaciones_contratos (
    id_mod          SERIAL PRIMARY KEY,
    num_contrato    VARCHAR(20) NOT NULL,
    fecha_mod       DATE NOT NULL,
    descripcion     TEXT NOT NULL,
    FOREIGN KEY (num_contrato) REFERENCES contratos(num_contrato) ON DELETE CASCADE
);

-- (múltiples temas o conceptos por evento)
CREATE TABLE eventos_temas (
    id_evento       INTEGER NOT NULL,
    tema            VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_evento, tema),
    FOREIGN KEY (id_evento) REFERENCES eventos(id_evento) ON DELETE CASCADE
);

-- indices importantes

CREATE INDEX idx_eventos_id_cliente ON eventos(id_cliente);
CREATE INDEX idx_contratos_id_cliente ON contratos(id_cliente);
CREATE INDEX idx_contratos_id_evento ON contratos(id_evento);
CREATE INDEX idx_eventos_servicios_cod_servicio ON eventos_servicios(cod_servicio);
CREATE INDEX idx_eventos_empleados_id_empleado ON eventos_empleados(id_empleado);
CREATE INDEX idx_contratos_lineas_cod_servicio ON contratos_lineas(cod_servicio);
CREATE INDEX idx_pagos_num_contrato ON pagos(num_contrato);
CREATE INDEX idx_modificaciones_num_contrato ON modificaciones_contratos(num_contrato);
CREATE INDEX idx_eventos_temas_id_evento ON eventos_temas(id_evento);
CREATE INDEX idx_servicios_productos_id_proveedor ON servicios_productos(id_proveedor);
