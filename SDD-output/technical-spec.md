# Technical Spec — Gud Boys (Refugio Animal)
> Version: 1.2 | Fecha: 2026-06-20 | Estado: Draft
>
> **Changelog v1.2:** `GestionAlarmaService` completo (`crearAlarma`, `actualizarAlarma` con `@Transactional`, `listarAlarmasPorAnimal`). Nuevo método `AlarmaMapper.actualizarEntity(dto, alarma)` para actualizar in-place sin duplicar filas. Ver sección [12](#12-gaps-detectados-todo) actualizada con estado real de cada service.
>
> **Changelog v1.1:** `VisitaDomicilio` ahora extiende `Evento` (herencia JOINED) para que las visitas domiciliarias queden en el historial de la ficha médica (RN-14). Ver [TD-05](#td-05--visitadomicilio-como-evento-rn-14).

## 1. Resumen Técnico

API REST construida en **Java con Spring Boot 3**, exponiendo 4 controllers principales (Animal, Alarma, Adopcion, Seguimiento). Arquitectura en capas (Controller → Service → Repository → Domain). Persistencia con **JPA/Hibernate** sobre **MySQL**. La capa de infraestructura implementa adaptadores para exportación (PDF/Excel), notificaciones (Firebase push, SMS, WhatsApp, Email) y el módulo de autenticación externo.

---

## 2. Stack y Versiones

| Tecnología | Versión | Rol |
|------------|---------|-----|
| Java | 17 (LTS) | Lenguaje base |
| Spring Boot | 3.3.x | Framework web y de inyección |
| Spring Data JPA | 3.3.x | Acceso a datos / ORM |
| Hibernate | 6.x [INFERRED] | Implementación JPA |
| MySQL | 8.x | Base de datos relacional |
| H2 | 2.x | Base de datos en memoria para tests |
| Lombok | 1.18.x | Reducción de boilerplate |
| Jakarta Validation | 3.x | Validación de DTOs |
| Maven | 3.9.x | Build tool |

---

## 3. Arquitectura

**Estilo:** Arquitectura en capas (Layered Architecture) + separación de dominio.

```
com.gudboys/
├── controller/          # Capa de presentación — recibe HTTP, delega a service
├── dto/
│   ├── request/         # Objetos de entrada de la API
│   └── response/        # Objetos de salida de la API
├── mapper/              # Conversión entre DTOs y entidades de dominio
├── service/             # Interfaces de servicio (contratos)
│   └── impl/            # Implementaciones (lógica de negocio — a completar)
├── domain/              # Entidades JPA y enums de dominio
│   └── enums/           # Enumeraciones del dominio
├── repository/          # Interfaces Spring Data JPA
├── infrastructure/
│   ├── export/          # ExportadorPDF, ExportadorExcel
│   ├── notification/    # FirebasePush, SmsSender, WhatsAppSender, EmailSender
│   └── auth/            # AuthServiceAdapter
└── config/              # Configuración de base de datos y beans
```

---

## 4. Modelo de Datos

### Animal (tabla: `animales`) — abstract
| Campo | Tipo | Notas |
|-------|------|-------|
| id | BIGINT PK | Auto-generado |
| altura | DOUBLE | En metros |
| peso | DOUBLE | En kg |
| edad | DOUBLE | Aproximada en años |
| condicion_medica | BOOLEAN | true = necesita atención médica |
| dtype | VARCHAR | Discriminador JPA (JOINED inheritance) |

**Relaciones:** `FichaMedica` (1:1), `Alarma` (1:N)

### AnimalDomestico (tabla: `animales_domesticos`)
| Campo | Tipo |
|-------|------|
| id | FK → animales.id |
| especie | VARCHAR | perro, gato, canario, loro, tortuga, etc. |

### AnimalSalvaje (tabla: `animales_salvajes`)
| Campo | Tipo |
|-------|------|
| id | FK → animales.id |
| especie | VARCHAR | zorro, pingüino, halcón, etc. |

### FichaMedica (tabla: `fichas_medicas`)
| Campo | Tipo | Notas |
|-------|------|-------|
| id | BIGINT PK | |
| animal_id | BIGINT FK | → animales.id, UNIQUE |

**Relaciones:** `Evento` (1:N)

### Evento (tabla: `eventos`) — abstract
| Campo | Tipo | Notas |
|-------|------|-------|
| id | BIGINT PK | |
| descripcion | VARCHAR | |
| fecha_hora | DATETIME | |
| ficha_medica_id | BIGINT FK | → fichas_medicas.id |
| dtype | VARCHAR | Discriminador (JOINED inheritance) |

### RegistroAtencion (tabla: `registros_atencion`)
| Campo | Tipo | Notas |
|-------|------|-------|
| id | BIGINT FK | → eventos.id |
| comentario | TEXT | |
| tratamiento_finalizado | BOOLEAN | null si no es tratamiento |
| veterinario_id | BIGINT FK | → veterinarios.id |

**Relaciones:** `AccionAlarma[]` (ElementCollection en tabla `registro_acciones`)

### Alarma (tabla: `alarmas`)
| Campo | Tipo | Notas |
|-------|------|-------|
| id | BIGINT PK | |
| periodicidad | INT | En días |
| es_tratamiento_medico | BOOLEAN | |
| estado | ENUM(ACTIVA,ATENDIDA,FINALIZADA) | |
| animal_id | BIGINT FK | → animales.id |

**Relaciones:** `AccionAlarma[]` (ElementCollection en tabla `alarma_acciones`)

### Alerta (tabla: `alertas`)
| Campo | Tipo |
|-------|------|
| id | BIGINT PK |
| alarma_id | BIGINT FK |
| fecha_disparo | DATETIME |
| atendida | BOOLEAN |

### Usuario (tabla: `usuarios`) — abstract
| Campo | Tipo | Notas |
|-------|------|-------|
| id | BIGINT PK | |
| external_id | VARCHAR | Referencia al módulo de autenticación externo |
| nombre | VARCHAR | |
| email | VARCHAR | |
| dtype | VARCHAR | Discriminador |

### Veterinario (tabla: `veterinarios`)
| Campo | Tipo |
|-------|------|
| id | BIGINT FK | → usuarios.id |

### Visitador (tabla: `visitadores`)
| Campo | Tipo |
|-------|------|
| id | BIGINT FK | → usuarios.id |

### Adoptante (tabla: `adoptantes`)
| Campo | Tipo | Notas |
|-------|------|-------|
| id | BIGINT PK | |
| nombre | VARCHAR | |
| apellido | VARCHAR | |
| estado_civil | VARCHAR | |
| email | VARCHAR | |
| telefono | VARCHAR | |
| ocupacion | ENUM(EMPLEADO,ESTUDIANTE,OTROS) | |
| tiene_mascotas | BOOLEAN | |
| motivo_adopcion | TEXT | |
| tipos_animales_interesados | VARCHAR | [INFERRED] almacenado como JSON o separado por comas |
| cant_animales_adoptados | INT | Validar ≤ 2 en service (RN-09) |

### Adopcion (tabla: `adopciones`)
| Campo | Tipo |
|-------|------|
| id | BIGINT PK |
| adoptante_id | BIGINT FK |
| animal_id | BIGINT FK | → animales_domesticos.id |
| fecha_adopcion | DATE |

### SeguimientoVisitas (tabla: `seguimientos_visitas`)
| Campo | Tipo | Notas |
|-------|------|-------|
| id | BIGINT PK | |
| adopcion_id | BIGINT FK UNIQUE | |
| visitador_id | BIGINT FK | |
| dia_visita | DATE | |
| horario_visita | VARCHAR | Ej: "14:00-16:00" |
| preferencia_recordatorio | ENUM(SMS,WHATSAPP,EMAIL) | |

### VisitaDomicilio (tabla: `visitas_domicilio`) — extiende `Evento`
| Campo | Tipo | Notas |
|-------|------|-------|
| id | BIGINT FK | → eventos.id (subtipo JOINED de `Evento`) |
| seguimiento_id | BIGINT FK | → seguimientos_visitas.id |
| fecha_visita | DATE | Fecha de la visita en sí (distinta de `eventos.fecha_hora`, que es cuándo se registró) |
| continuar_visitas | BOOLEAN | |

**Herencia:** Al ser subtipo de `Evento`, hereda `descripcion`, `fecha_hora` y `ficha_medica_id` (tabla `eventos`). Por eso una visita queda enlazada **a la vez** a su `SeguimientoVisitas` (vía `seguimiento_id`) y a la `FichaMedica` del animal (vía `ficha_medica_id` heredado), apareciendo en el historial unificado de la ficha (RN-14). Ver [TD-05](#td-05--visitadomicilio-como-evento-rn-14).

### EncuestaSeguimiento (tabla: `encuestas_seguimiento`)
| Campo | Tipo |
|-------|------|
| id | BIGINT PK |
| visita_id | BIGINT FK UNIQUE |
| estado_general | ENUM(BUENO,REGULAR,MALO) |
| limpieza_lugar | ENUM(BUENO,REGULAR,MALO) |
| ambiente | ENUM(BUENO,REGULAR,MALO) |

---

## 5. API Surface

| Método | Path | Descripción | Request DTO | Response DTO |
|--------|------|-------------|-------------|--------------|
| POST | /api/animales | Ingresar animal | `IngresarAnimalRequestDTO` | `AnimalResponseDTO` |
| GET | /api/animales | Listar animales | — | `List<AnimalResponseDTO>` |
| GET | /api/animales/{id} | Obtener animal | — | `AnimalResponseDTO` |
| GET | /api/animales/{id}/ficha-medica | Ver ficha médica | — | `FichaMedicaResponseDTO` |
| GET | /api/animales/{id}/ficha-medica/exportar | Exportar ficha (param: formato=PDF\|EXCEL) | — | Archivo binario |
| POST | /api/animales/{animalId}/alarmas | Crear alarma | `CrearAlarmaRequestDTO` | `AlarmaResponseDTO` |
| GET | /api/animales/{animalId}/alarmas | Listar alarmas del animal | — | `List<AlarmaResponseDTO>` |
| PUT | /api/alarmas/{id} | Actualizar alarma | `CrearAlarmaRequestDTO` | `AlarmaResponseDTO` |
| POST | /api/alarmas/{id}/atender | Atender alarma | `AtenderAlarmaRequestDTO` | `AlarmaResponseDTO` |
| POST | /api/adopciones | Registrar adopción | `RegistrarAdopcionRequestDTO` | `AdopcionResponseDTO` |
| GET | /api/adopciones | Listar adopciones | — | `List<AdopcionResponseDTO>` |
| GET | /api/adopciones/{id} | Obtener adopción | — | `AdopcionResponseDTO` |
| POST | /api/seguimientos | Configurar seguimiento | `ConfigurarSeguimientoRequestDTO` | `SeguimientoResponseDTO` |
| GET | /api/seguimientos/{id} | Obtener seguimiento | — | `SeguimientoResponseDTO` |
| POST | /api/seguimientos/{id}/visitas | Registrar visita | `RegistrarVisitaRequestDTO` | `VisitaResponseDTO` |
| GET | /api/seguimientos/{id}/visitas | Listar visitas | — | `List<VisitaResponseDTO>` |

**Autenticación:** [TODO] Definir cómo se valida el token del módulo externo (Bearer JWT asumido [ASSUMPTION]).

---

## 6. Dependencias Externas

| Servicio | Rol | Adaptador |
|----------|-----|-----------|
| Módulo de Autenticación | Login/registro de Veterinario y Visitador | `AuthServiceAdapter` |
| Firebase Cloud Messaging | Push notifications a veterinarios | `FirebasePushNotification` |
| SMS Gateway | Recordatorios por SMS | `SmsSender` |
| WhatsApp Business API | Recordatorios por WhatsApp | `WhatsAppSender` |
| Email SMTP | Recordatorios por email | `EmailSender` |

---

## 7. Configuración y Variables de Entorno

```properties
# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/gudboys
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Recordatorios
gudboys.recordatorio.dias-anticipacion=3

# Auth externo
gudboys.auth.service-url=${AUTH_SERVICE_URL}

# Firebase [TODO]
gudboys.firebase.server-key=${FIREBASE_SERVER_KEY}
```

---

## 8. Decisiones Técnicas (TD-XX)

### TD-01 — Herencia JPA con JOINED strategy
- **Contexto:** Animal tiene dos subtipos (Doméstico/Salvaje) con campos distintos. Lo mismo para Usuario (Veterinario/Visitador) y Evento (RegistroAtencion/VisitaDomicilio como evento).
- **Decisión:** Usar `@Inheritance(strategy = InheritanceType.JOINED)`. Cada subtipo tiene su propia tabla con FK al padre.
- **Trade-off:** Más queries con JOINs vs. claridad del modelo y ausencia de columnas nulas. Para este volumen de datos, el overhead es negligible.
- **Status:** Confirmado.

### TD-02 — AccionAlarma como Enum + ElementCollection
- **Contexto:** El diseño original propone Composite para las acciones de alarma.
- **Decisión:** Modelar `AccionAlarma` como `enum` con una tabla de colección (`@ElementCollection`). Un `List<AccionAlarma>` en Alarma y en RegistroAtencion es suficiente para los requerimientos.
- **Trade-off:** Menos flexibilidad que Composite, pero mucho más simple. Si se necesitan acciones con comportamiento propio (no solo datos), refactorizar a Strategy/Composite.
- **Status:** Confirmado [INFERRED que el equipo prefiere simplicidad].

### TD-03 — Referencia a Usuario externo por externalId
- **Contexto:** El módulo de autenticación gestiona usuarios. Nuestro sistema no puede crear ni modificar credenciales.
- **Decisión:** Guardar `externalId` (String) en la entidad `Usuario`. El `AuthServiceAdapter` se usa para obtener datos del usuario cuando sea necesario.
- **Trade-off:** Acoplamiento ligero al servicio externo; si el servicio cambia, solo se modifica el adaptador.
- **Status:** Confirmado por enunciado.

### TD-04 — Patrones de Diseño a implementar [TODO por el equipo]
- **Contexto:** El TP exige patrones de diseño. El enunciado implica Strategy (exportación), Observer (notificaciones), Strategy (recordatorios). El TPO define también State para Alarma.
- **Decisión:** Las interfaces están creadas (`IExportadorStrategy`, `IRecordatorioStrategy`, `INotificadorPush`). Las implementaciones quedan pendientes. `GestionAlarmaService` ya está completo pero usa el enum `EstadoAlarma` plano, sin el patrón State — la lógica de transición de estados todavía no está encapsulada.
- **Trade-off:** Sin los patrones, la exportación y notificaciones no funcionarán. El State pattern para `Alarma` sigue pendiente y RN-07 (bloqueo de adopción según estado de tratamiento) se va a resolver con `if/switch` hasta que se implemente.
- **Status:** [TODO] — Implementación pendiente por el equipo.

### TD-06 — `AlarmaMapper.actualizarEntity` para actualización in-place
- **Contexto:** `actualizarAlarma` necesitaba aplicar los cambios de un `CrearAlarmaRequestDTO` sobre una `Alarma` ya persistida, sin crear una entidad nueva (lo que generaría un `INSERT` duplicado en vez de un `UPDATE`).
- **Decisión:** Se agregó `AlarmaMapper.actualizarEntity(CrearAlarmaRequestDTO dto, Alarma alarma)`, que muta in-place `periodicidad`, `esTratamientoMedico` y `acciones` sobre la instancia recibida (managed por JPA) y la retorna. No toca `id`, `animal` ni `estado` — esos campos no se actualizan por este flujo.
- **Trade-off:** Mantiene `save()` haciendo `UPDATE` sobre la misma fila/PK en vez de duplicar registros. Como contrapartida, si en el futuro se necesita reasignar el animal o forzar un cambio de estado vía esta misma operación, hay que extender el método o agregar uno nuevo — hoy `actualizarEntity` ignora esos campos deliberadamente.
- **Status:** Confirmado — implementado y en uso por `GestionAlarmaService.actualizarAlarma`.

### TD-05 — VisitaDomicilio como Evento (RN-14)
- **Contexto:** RN-14 exige que el historial de la ficha médica unifique **registros de atención veterinaria y visitas domiciliarias**. En el modelo inicial, `VisitaDomicilio` era una entidad independiente colgada solo de `SeguimientoVisitas`, sin relación con `FichaMedica`. Era imposible que una visita apareciera en el historial: `FichaMedica.eventos` es `List<Evento>` y `VisitaDomicilio` no era un `Evento`. Esto contradecía tanto RN-14 como la intención ya expresada en [TD-01](#td-01--herencia-jpa-con-joined-strategy) ("VisitaDomicilio como evento").
- **Decisión:** Hacer que `VisitaDomicilio` extienda `Evento` (herencia JOINED, con `@PrimaryKeyJoinColumn`, igual que `RegistroAtencion`). La visita pasa a tener dos relaciones simultáneas: `SeguimientoVisitas` (su cadencia/contexto de seguimiento) y `FichaMedica` (heredada de `Evento`, para el historial). Su PK deja de ser `IDENTITY` propia y pasa a ser FK a `eventos.id`.
- **Trade-off:** Alinea el código con RN-14 y TD-01 a costa de un cambio estructural de schema. Con `ddl-auto=update` la PK de `visitas_domicilio` no se migra automáticamente: en entornos existentes hay que recrear las tablas `visitas_domicilio` y `encuestas_seguimiento`. Como el feature no tenía datos productivos, el impacto es nulo. El `AnimalMapper` puede sumar una rama para exponer datos propios de la visita (encuesta, `continuar_visitas`) en el historial; sin ella, la visita igualmente aparece con su `tipo` y `descripcion`.
- **Status:** Confirmado — refactor en curso por el equipo.

---

## 9. Deployment y Runtime

- **Tipo:** JAR ejecutable (`java -jar gudboys.jar`)
- **Base de datos:** MySQL 8.x en el mismo servidor o contenedor Docker
- **Arranque local:** `mvn spring-boot:run` con variables de entorno configuradas
- **Perfil de test:** `application-test.properties` con H2 en memoria

---

## 10. Observabilidad

- [TODO] Spring Boot Actuator para endpoints de health y métricas (`/actuator/health`)
- [TODO] Logging con SLF4J + Logback (incluido por defecto en Spring Boot)
- [TODO] Trazabilidad de atenciones de alarma mediante `RegistroAtencion` (persistido en BD)

---

## 11. Testing

- [TODO] Tests unitarios con JUnit 5 + Mockito para services
- [TODO] Tests de integración con @SpringBootTest y H2
- Los tests deben cubrir: ingreso de animal, ciclo de vida de alarma, validación de adopción (animal salvaje, bajo tratamiento, límite de 2)

---

## 12. Gaps Detectados [TODO]

**Estado de los Services (verificado en código, 2026-06-20):**

| Service | Estado |
|---------|--------|
| `IngresoAnimalService` | Completo |
| `GestionAlarmaService` | **Completo** — `crearAlarma`, `actualizarAlarma` (`@Transactional`), `listarAlarmasPorAnimal` |
| `AtencionAlarmaService` | Completo (sin excepciones específicas — usa `RuntimeException` genérica) |
| `AdopcionService` | Completo |
| `VisitaSeguimientoService` | Casi completo — `configurarSeguimiento`, `obtenerSeguimiento`, `registrarVisita` y `listarVisitas` listos. Solo falta `enviarRecordatoriosProximos` (CU-08), que depende de que se implemente `IRecordatorioStrategy` |

- [TODO] Implementar el método de recordatorios en `VisitaSeguimientoService` (depende de `IRecordatorioStrategy`)
- [TODO] Implementar IExportadorStrategy en ExportadorPDF y ExportadorExcel
- [TODO] Implementar IRecordatorioStrategy en SmsSender, WhatsAppSender, EmailSender
- [TODO] Implementar FirebasePushNotification (Observer del veterinario)
- [TODO] Implementar AuthServiceAdapter para obtener datos de usuarios externos
- [TODO] Implementar el job/scheduler para disparar alarmas automáticamente (¿Spring @Scheduled?)
- [TODO] Definir manejo de excepciones global (@ControllerAdvice)
- [TODO] Implementar seguridad con JWT si el módulo externo lo usa
- [TODO] Migración de base de datos con Flyway o Liquibase [ASSUMPTION: no requerido por el TP]

---

## 13. Supuestos a Validar

- [ ] ¿MySQL es la base de datos elegida o prefieren PostgreSQL / H2 puro?
- [ ] ¿El externalId del usuario es un UUID String o Long?
- [ ] ¿El scheduler de alarmas es parte del TP o se asume disparo manual?
- [ ] ¿Se requiere seguridad HTTP (Spring Security) o el enunciado lo excluye?
- [ ] ¿Los tipos de animales interesados del adoptante son un enum o texto libre?
