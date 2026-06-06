PRAGMA foreign_keys = ON;

-- Insert categorías de servicios/productos
INSERT OR IGNORE INTO categorias_servicios_productos (id_categoria, nombre) VALUES
(1, 'Floral'),
(2, 'Mobiliario'),
(3, 'Iluminación');

-- Insert proveedores
INSERT OR IGNORE INTO proveedores (id_proveedor, nombre, tipo_servicio, direccion, telefono, email, responsable) VALUES
(1, 'Flores del Valle', 'Floristería', 'Calle 23 #456, Plaza', '555-1234', 'contacto@floresdelvalle.com', 'María López'),
(2, 'Muebles Elegantes', 'Mobiliario', 'Avenida 51 #789, Playa', '555-5678', 'info@muebleselegantes.com', 'Carlos Ruiz');

-- Insert clientes
INSERT OR IGNORE INTO clientes (id_cliente, nombre, apellidos, num_documento, direccion, telefono, email, trato_pref) VALUES
(1, 'Juan', 'Pérez González', '12345678', 'Calle A #123, Vedado', '555-1111', 'juan.perez@example.com', 1),
(2, 'Ana', 'Martínez López', '87654321', 'Calle B #456, Miramar', '555-2222', 'ana.martinez@example.com', 0),
(3, 'Pedro', 'Rodríguez Gómez', '11223344', 'Calle C #789, Centro', '555-3333', 'pedro.rodriguez@example.com', 0);

-- Insert empleados
INSERT OR IGNORE INTO empleados (id_empleado, nombre, apellidos, num_documento, direccion, telefono, email, cargo, departamento) VALUES
(1, 'Laura', 'Fernández Díaz', '55443322', 'Calle D #101, Nuevo Vedado', '555-4444', 'laura.fernandez@decobana.com', 'Coordinadora de Eventos', 'Operaciones'),
(2, 'Carlos', 'Mendoza Reyes', '66778899', 'Calle E #202, Cerro', '555-5555', 'carlos.mendoza@decobana.com', 'Diseñador', 'Creatividad'),
(3, 'Marta', 'Iglesias Suárez', '99887766', 'Calle F #303, Lawton', '555-6666', 'marta.iglesias@decobana.com', 'Asistente', 'Operaciones');

-- Insert servicios/productos (requiere categorías y proveedores)
INSERT OR IGNORE INTO servicios_productos (cod_servicio, nombre, descripcion, id_categoria, precio_unitario, es_tercero, estado_activo, id_proveedor) VALUES
('S001', 'Ramo de rosas', 'Ramo de 24 rosas rojas', 1, 45.00, 1, 1, 1),
('S002', 'Sillas tapizadas', 'Sillas elegantes con funda', 2, 12.50, 1, 1, 2),
('S003', 'Iluminación LED', 'Set de luces RGB', 3, 80.00, 0, 1, NULL),
('S004', 'Centro de mesa floral', 'Arreglo floral para centro de mesa', 1, 35.00, 1, 1, 1);

-- Insert eventos
INSERT OR IGNORE INTO eventos (id_evento, nombre_evento, tipo_evento, fecha_hora_ini, fecha_hora_fin, ubicacion, num_invitados, id_cliente) VALUES
(1, 'Boda de Juan y María', 'Boda', '2025-06-15 16:00:00', '2025-06-15 23:00:00', 'Quinta Avenida, Playa', 120, 1),
(2, 'Aniversario Empresarial', 'Corporativo', '2025-07-10 10:00:00', '2025-07-10 18:00:00', 'Hotel Nacional', 200, 2);

-- Insert temas de eventos
INSERT OR IGNORE INTO eventos_temas (id_evento, tema) VALUES
(1, 'Rústico'),
(1, 'Romántico'),
(2, 'Elegante'),
(2, 'Tecnológico');

-- Insert servicios utilizados en eventos
INSERT OR IGNORE INTO eventos_servicios (id_evento, cod_servicio, cantidad) VALUES
(1, 'S001', 10),
(1, 'S002', 120),
(1, 'S003', 5),
(2, 'S002', 200),
(2, 'S003', 10),
(2, 'S004', 25);

-- Insert empleados asignados a eventos
INSERT OR IGNORE INTO eventos_empleados (id_evento, id_empleado, responsabilidades) VALUES
(1, 1, 'Supervisión general'),
(1, 2, 'Diseño floral'),
(1, 3, 'Atención a proveedores'),
(2, 1, 'Coordinación'),
(2, 2, 'Dirección creativa');

-- Insert contratos
INSERT OR IGNORE INTO contratos (num_contrato, fecha_contrato, terminos_cond, id_cliente, id_evento) VALUES
('C001', '2025-05-01 00:00:00', 'Pago anticipado del 30%, saldo a 15 días después del evento', 1, 1),
('C002', '2025-06-01 00:00:00', 'Pago único a 30 días', 2, 2);

-- Insert líneas de contratos (precios negociados)
INSERT OR IGNORE INTO contratos_lineas (num_contrato, cod_servicio, cantidad, precio_negociado) VALUES
('C001', 'S001', 10, 42.00),
('C001', 'S002', 120, 11.00),
('C001', 'S003', 5, 75.00),
('C002', 'S002', 200, 12.00),
('C002', 'S003', 10, 78.00),
('C002', 'S004', 25, 33.00);

-- Insert pagos
INSERT OR IGNORE INTO pagos (id_pago, num_contrato, fecha_pago, monto) VALUES
(1, 'C001', '2025-05-01 00:00:00', 1500.00),
(2, 'C001', '2025-06-30 00:00:00', 3500.00),
(3, 'C002', '2025-06-01 00:00:00', 4500.00);

-- Insert modificaciones de contratos
INSERT OR IGNORE INTO modificaciones_contratos (id_mod, num_contrato, fecha_mod, descripcion) VALUES
(1, 'C001', '2025-05-10 00:00:00', 'Se incrementaron 20 sillas adicionales'),
(2, 'C002', '2025-06-05 00:00:00', 'Se cambió el menú de catering por uno más amplio');
