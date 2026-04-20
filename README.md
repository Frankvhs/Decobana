# Decobana

Sistema de base de datos para una empresa de decoración de fiestas y eventos. Proyecto final de Base de Datos para [**Segundo año de ingeniería informatica de la CUJAE**](https://moodle.cujae.edu.cu/course/index.php?categoryid=49)

- **Empresa:** Decobana (ficticia)
- **Tecnología:** SQLite + Java (próximamente)

## Estructura del Proyecto

```
.
├── db/
│   └── migrations/          # Scripts SQL de migración
├── docs/
│   ├── DIAGRAMA.md          # Diagrama entidad-relación
│   └── ORIENTACION.md       # Especificación del proyecto
└── README.md
```

## Entidades Principales

- Empresa
- Clientes
- Proveedores
- Eventos
- Servicios/Productos
- Empleados
- Contratos

## Probar la Migración

```bash
# Crear base de datos y ejecutar migración
sqlite3 decobana.db < db/migrations/20260420142936_initial_db.sql

# Verificar tablas creadas
sqlite3 decobana.db ".tables"

# Ver esquema completo
sqlite3 decobana.db ".schema"
```

## Estado del Proyecto

- [x] Modelo de base de datos
- [x] Migración SQL inicial
- [ ] Implementación Java

---

_Proyecto para la CUJAE - Entrega final Base de Datos_
