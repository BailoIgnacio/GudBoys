# Technical Spec — Gud Boys (Refugio Animal)
> Version: 1.5 | Fecha: 2026-06-30 | Estado: Draft
>
> **Changelog v1.5:** Tres bloques de cambios. (1) **Relación Animal↔FichaMedica simplificada** — eliminada la bidireccional `@OneToOne(mappedBy)` de `Animal`; la relación queda unidireccional (`FichaMedica → Animal`). `AnimalMapper`, `SeguimientoMapper` y `AdopcionService` ahora usan `fichaMedicaRepository.findByAnimalId()` en vez de `animal.getFichaMedica()`. Nueva decisión técnica [TD-07](#td-07--relación-unidireccional-fichamedica--animal). (2) **Patrones State, Composite y Observer implementados** — `Alarma.estado` migrado a `IEstadoAlarma` con `EstadoAlarmaConverter`; `AccionFactory` reconstruye el árbol Composite en memoria desde el enum persistido; `Alerta` implementa `IObservado`, `FirebasePushNotification` implementa `INotificadorPushObservador` (log simulado), `GestionAlarmaService.generarAlerta()` orquesta el flujo Observer completo; `INotificadorPush` eliminado. (3) **Scheduler de alarmas implementado** — `AlarmaScheduler` con `@Scheduled` + `GestionAlarmaService.dispararAlarmasVencidas()` + nuevo campo `fechaProximoDisparo` en `Alarma`. Se actualizan [§12](#12-gaps-detectados-todo), [§14](#14-trabajo-restante-para-cerrar-el-proyecto) y [§15](#15-patrones-de-diseño--diseño-objetivo-pds-tpmdj).
>
> **Changelog v1.4:** Incorporado el diseño objetivo del diagrama UML actualizado (`PDS-TP.mdj`). Se documentan los **patrones de diseño** que define el diagrama en la nueva sección [15](#15-patrones-de-diseño--diseño-objetivo-pds-tpmdj): **State** (`IEstadoAlarma`), **Composite** (`ComponenteAccion`), **Strategy + Decorator + Factory** para exportación (con encriptado y marca de agua), **Factory** para recordatorios, **Observer** (`IObservado`/`IObservador`) y **Adapter**. [TD-02](#td-02--accionalarma-como-enum--elementcollection) queda **superado** (el diagrama vuelve a Composite). [TD-04](#td-04--patrones-de-diseño-a-implementar-todo-por-el-equipo) y [§14](#14-trabajo-restante-para-cerrar-el-proyecto) ahora apuntan al diseño concreto. Se marca cada **divergencia entre el código actual y el diagrama objetivo**.
>
> **Changelog v1.3:** Auditoría completa del código. **Toda la lógica de negocio de los 5 services está implementada** salvo `enviarRecordatoriosProximos` (CU-08). Se actualizan la sección [12](#12-gaps-detectados-todo) (estado real de services e infraestructura) y [TD-04](#td-04--patrones-de-diseño-a-implementar-todo-por-el-equipo) (qué patrones faltan). `GlobalExceptionHandler` (`@RestControllerAdvice`) ya existe pero solo cubre `AnimalNotFoundException`. **Lo único pendiente para terminar el TP son los patrones de diseño (Strategy ×2, Observer, State) + el scheduler.** Ver nueva sección [14](#14-trabajo-restante-para-cerrar-el-proyecto).
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

**Relaciones:** `Alarma` (1:N). La relación con `FichaMedica` es **unidireccional** desde `FichaMedica` hacia `Animal` — `Animal` no tiene referencia a `FichaMedica`. Ver [TD-07](#td-07--relación-unidireccional-fichamedica--animal).

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
| estado | VARCHAR | Persistido como `"ACTIVA"/"ATENDIDA"/"FINALIZADA"` vía `EstadoAlarmaConverter`; en memoria es `IEstadoAlarma` (patrón State). Ver [§15.1](#151-state--estado-de-la-alarma). |
| animal_id | BIGINT FK | → animales.id |
| fecha_proximo_disparo | DATETIME | Calculada al crear/reprogramar. El scheduler la compara contra `now()` para detectar alarmas vencidas. |

**Relaciones:** `AccionAlarma[]` (ElementCollection en tabla `alarma_acciones`) como discriminador de persistencia. En memoria se reconstruye el árbol Composite vía `AccionFactory.crearComposite()`; ver [§15.2](#152-composite--acciones-de-la-alarma).

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
| GET | /api/animales/{id}/ficha-medica/exportar | Exportar ficha (param: formato=PDF\|EXCEL) — ❌ **no implementado** (sin endpoint en `AnimalController`, depende del Strategy de exportación) | — | Archivo binario |
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

### TD-02 — AccionAlarma: Enum como persistencia + Composite en memoria (v1.5)
- **Contexto:** El diseño original usaba el enum como única representación. El diagrama objetivo pide Composite.
- **Decisión (v1.5):** Enfoque híbrido — el **enum `AccionAlarma` + `@ElementCollection`** se mantiene como discriminador de persistencia (simple, sin migrations); `AccionFactory.crearComposite(List<AccionAlarma>)` reconstruye el árbol de `ComponenteAccion` en memoria. `Alarma.getAccionesComposite()` (campo `@Transient`) expone el árbol. `AtencionAlarmaService` usa `AccionFactory.crearComposite(dto.getAccionesRealizadas()).getDescripcion()` para generar la descripción del `RegistroAtencion`.
- **Trade-off:** Evita migrar la tabla `alarma_acciones` y mantiene la simplicidad de persistencia, a costa de que el Composite no es la fuente de verdad en BD. Si se necesitara persistir el árbol completo (acciones compuestas anidadas), habría que migrar a herencia JPA para `ComponenteAccion`.
- **Status:** Implementado — `AccionFactory` y `AccionComposite` en uso.

### TD-03 — Referencia a Usuario externo por externalId
- **Contexto:** El módulo de autenticación gestiona usuarios. Nuestro sistema no puede crear ni modificar credenciales.
- **Decisión:** Guardar `externalId` (String) en la entidad `Usuario`. El `AuthServiceAdapter` se usa para obtener datos del usuario cuando sea necesario.
- **Trade-off:** Acoplamiento ligero al servicio externo; si el servicio cambia, solo se modifica el adaptador.
- **Status:** Confirmado por enunciado.

### TD-04 — Patrones de Diseño a implementar [TODO por el equipo]
- **Contexto:** El TP exige patrones de diseño. El enunciado implica Strategy (exportación), Observer (notificaciones), Strategy (recordatorios). El TPO define también State para Alarma.
- **Decisión:** Las interfaces/contratos están creados (`IExportadorStrategy`, `IRecordatorioStrategy`, `INotificadorPush`) y las clases concretas existen registradas como `@Component`, pero **todas son stubs** que lanzan `UnsupportedOperationException`. La lógica de negocio que las debería orquestar ya está lista y a la espera de ellas.
- **Estado por patrón (verificado al 2026-06-22):**

  | Patrón | Dónde | Estado | Qué falta |
  |--------|-------|--------|-----------|
  | **Strategy — Exportación** | `IExportadorStrategy` → `ExportadorPDF`, `ExportadorExcel` | ❌ Stubs | Implementar `exportar()`, agregar deps (iText/PDFBox y Apache POI) al `pom.xml`, crear el selector de estrategia por `getFormato()` y exponer el endpoint `GET .../ficha-medica/exportar` |
  | **Strategy — Recordatorios** | `IRecordatorioStrategy` → `SmsSender`, `WhatsAppSender`, `EmailSender` | ❌ Stubs | Implementar `enviarRecordatorio()`, selector por `getCanal()` mapeado a `PreferenciaRecordatorio`, y `VisitaSeguimientoService.enviarRecordatoriosProximos()` |
  | **Observer — Notificación a veterinarios** | `INotificadorPush` → `FirebasePushNotification` | ❌ Stub, sin disparo | Implementar `notificar()`, generar la `Alerta`, e invocarlo cuando se dispara una alarma (no se llama desde ningún flujo hoy) |
  | **State — Estado de Alarma** | `EstadoAlarma` (enum plano) → diagrama: `IEstadoAlarma` + `EstadoActiva`/`EstadoAtendida`/`EstadoFinalizada` | ❌ No aplicado | Encapsular las transiciones `ACTIVA → ATENDIDA → FINALIZADA` en clases de estado. Hoy la transición vive en `AtencionAlarmaService` con un `if/else` y el bloqueo de adopción (RN-07) en `esAdoptable()`/`estaBajoTratamientoActivo()` comparando el enum |

- **Trade-off:** La funcionalidad cumplida (CU-01, 03, 05, 06, 07 + atención de CU-04) **no depende** de estos patrones, por eso ya funciona. Lo que queda bloqueado es exactamente lo que estos patrones habilitan: exportación (CU-02/RN-03), recordatorios (CU-08/RN-12) y disparo+push de alarmas (CU-04/RN-05). El State es el único "opcional desde lo funcional" (RN-07 ya se cumple con el enum), pero el TP lo pide explícitamente.
- **Actualización (v1.4):** El diagrama `PDS-TP.mdj` agrega además **Composite** (acciones), **Decorator** y **Factory** (exportación) y **Factory** (recordatorios), y formaliza **Observer** con `IObservado`/`IObservador`. El detalle de cada uno, con sus clases participantes, está en la nueva sección [15](#15-patrones-de-diseño--diseño-objetivo-pds-tpmdj).
- **Status:** [TODO] — Es el **trabajo principal restante** del proyecto. Ver secciones [14](#14-trabajo-restante-para-cerrar-el-proyecto) y [15](#15-patrones-de-diseño--diseño-objetivo-pds-tpmdj).

### TD-06 — `AlarmaMapper.actualizarEntity` para actualización in-place
- **Contexto:** `actualizarAlarma` necesitaba aplicar los cambios de un `CrearAlarmaRequestDTO` sobre una `Alarma` ya persistida, sin crear una entidad nueva (lo que generaría un `INSERT` duplicado en vez de un `UPDATE`).
- **Decisión:** Se agregó `AlarmaMapper.actualizarEntity(CrearAlarmaRequestDTO dto, Alarma alarma)`, que muta in-place `periodicidad`, `esTratamientoMedico` y `acciones` sobre la instancia recibida (managed por JPA) y la retorna. No toca `id`, `animal` ni `estado` — esos campos no se actualizan por este flujo.
- **Trade-off:** Mantiene `save()` haciendo `UPDATE` sobre la misma fila/PK en vez de duplicar registros. Como contrapartida, si en el futuro se necesita reasignar el animal o forzar un cambio de estado vía esta misma operación, hay que extender el método o agregar uno nuevo — hoy `actualizarEntity` ignora esos campos deliberadamente.
- **Status:** Confirmado — implementado y en uso por `GestionAlarmaService.actualizarAlarma`.

### TD-07 — Relación unidireccional FichaMedica → Animal (v1.5)
- **Contexto:** `Animal` tenía `@OneToOne(mappedBy = "animal") FichaMedica fichaMedica`, lo que generaba una bidireccional. Algunos lugares usaban `animal.getFichaMedica()` y otros `fichaMedicaRepository.findByAnimalId()`, sin criterio claro — código inconsistente y acoplamiento innecesario.
- **Decisión:** Eliminar el campo `fichaMedica` de `Animal`. La relación queda unidireccional: solo `FichaMedica` tiene `@JoinColumn(name="animal_id")`. Para obtener la ficha de un animal siempre se usa `fichaMedicaRepository.findByAnimalId(id)`.
- **Trade-off:** Reduce acoplamiento entre entidades y hace el código predecible (una sola forma de obtener la ficha). La BD no cambia (la FK `animal_id` en `fichas_medicas` ya existía y es la única FK). `AnimalMapper`, `SeguimientoMapper` y `AdopcionService` fueron actualizados para ir por el repositorio.
- **Status:** Implementado.

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

**Estado de los Services (verificado en código, 2026-06-30):**

| Service | Estado |
|---------|--------|
| `IngresoAnimalService` | ✅ Completo — `ingresarAnimal`, `obtenerAnimal`, `listarAnimales`, `obtenerFichaMedica`. Usa excepciones específicas (`AnimalException`, `AnimalNotFoundException`) |
| `GestionAlarmaService` | ✅ Completo — `crearAlarma`, `actualizarAlarma`, `listarAlarmasPorAnimal`, `generarAlerta` (Observer), `dispararAlarmasVencidas` (Scheduler) |
| `AtencionAlarmaService` | ✅ Completo — crea `RegistroAtencion` con descripción Composite, transiciona estado vía State, marca alertas pendientes como atendidas. *Pendiente menor:* excepciones genéricas (`RuntimeException`) |
| `AdopcionService` | ✅ Completo — valida RN-07/08/09 vía repositorio. *Pendiente menor:* excepciones genéricas |
| `VisitaSeguimientoService` | ⚠️ Casi completo — `configurarSeguimiento`, `obtenerSeguimiento`, `registrarVisita` y `listarVisitas` listos. Falta solo `enviarRecordatoriosProximos` (CU-08), depende de `IRecordatorioStrategy` |

**Estado de la Infraestructura:**

| Componente | Estado |
|------------|--------|
| `ExportadorPDF` / `ExportadorExcel` (`IExportadorStrategy`) | ❌ Stub (`UnsupportedOperationException`), sin deps en `pom.xml` |
| `SmsSender` / `WhatsAppSender` / `EmailSender` (`IRecordatorioStrategy`) | ❌ Stub |
| `FirebasePushNotification` (`INotificadorPushObservador`, Observer) | ✅ Implementado — log simulado. Invocado desde `GestionAlarmaService.generarAlerta()` |
| `AuthServiceAdapter` | ❌ Stub |
| `AlarmaScheduler` (`@Scheduled`, disparo de alarmas) | ✅ Implementado — `dispararAlarmasVencidas()` cada `gudboys.scheduler.alarmas.fixed-delay` ms (default 1h) |
| Scheduler recordatorios (`enviarRecordatoriosProximos`) | ❌ No existe |
| `GlobalExceptionHandler` (`@RestControllerAdvice`) | ⚠️ Existe, pero solo maneja `AnimalNotFoundException`; faltan `AnimalException` y un handler genérico |
| Tests (`src/test`) | ❌ Carpeta inexistente, cero tests |
| Seguridad / JWT | ❌ No existe |
| Flyway / Liquibase | ❌ No existe [ASSUMPTION: no requerido por el TP] |

**Pendientes priorizados:**
- [TODO] **(Patrón Strategy + Decorator + Factory)** Implementar `ExportadorPDF`/`ExportadorExcel` + deps PDF/Excel + endpoint de exportación → cierra CU-02/RN-03/RN-15
- [TODO] **(Patrón Strategy + Factory)** Implementar los 3 senders + `RecordatorioFactory` + `enviarRecordatoriosProximos` + scheduler de recordatorios → cierra CU-08/RN-12
- [TODO] Ampliar `GlobalExceptionHandler` (manejar `AnimalException` y un handler genérico; reemplazar `RuntimeException` por excepciones de dominio)
- [TODO] Implementar `AuthServiceAdapter` para obtener datos de usuarios externos
- [TODO] Tests unitarios/integración (JUnit 5 + Mockito + H2)
- [TODO] Seguridad con JWT si el módulo externo lo usa

---

## 13. Supuestos a Validar

- [ ] ¿MySQL es la base de datos elegida o prefieren PostgreSQL / H2 puro?
- [ ] ¿El externalId del usuario es un UUID String o Long?
- [ ] ¿El scheduler de alarmas es parte del TP o se asume disparo manual?
- [ ] ¿Se requiere seguridad HTTP (Spring Security) o el enunciado lo excluye?
- [ ] ¿Los tipos de animales interesados del adoptante son un enum o texto libre?

---

## 14. Trabajo Restante para Cerrar el Proyecto

La lógica de negocio y los principales patrones de diseño están implementados. **Lo que queda son los patrones de exportación y recordatorios**, más hardening. Orden sugerido:

### Bloque 1 — Patrones pendientes

1. **Strategy + Decorator + Factory — Exportación de ficha médica** *(CU-02, RN-03, RN-15)*
   - Agregar dependencias al `pom.xml`: iText o Apache PDFBox (PDF) y Apache POI (Excel).
   - Implementar `ExportadorPDF.exportar()` y `ExportadorExcel.exportar()` (Strategy).
   - Crear `ExportadorDecorator` + `EncriptarDecorator` + `MarcaAguaDecorator` (Decorator).
   - Crear `ExportadorFactory.crearExportador(formato, encriptar, agua)` que arma strategy + decoradores.
   - Exponer `GET /api/animales/{id}/ficha-medica/exportar?formato=PDF|EXCEL&encriptar=&agua=` en `AnimalController`.

2. **Strategy + Factory — Recordatorios de visita** *(CU-08, RN-12)*
   - Implementar `SmsSender`/`WhatsAppSender`/`EmailSender.enviarRecordatorio()`.
   - Crear `RecordatorioFactory` que selecciona la estrategia según `PreferenciaRecordatorio`.
   - Completar `VisitaSeguimientoService.enviarRecordatoriosProximos()`.
   - Agregar un segundo `@Scheduled` en un nuevo `RecordatorioScheduler` que llame a `enviarRecordatoriosProximos()`.

### Bloque 2 — Robustez (recomendado)

3. Ampliar `GlobalExceptionHandler` (manejar `AnimalException` + handler genérico) y migrar los `RuntimeException` a excepciones de dominio.
4. `AuthServiceAdapter` real (si el módulo externo lo requiere).
5. Tests (JUnit 5 + Mockito + H2): ciclo de vida de alarma, validaciones de adopción (salvaje, bajo tratamiento, límite 2).
6. Seguridad JWT (si aplica al alcance).

### Estado de avance de patrones

| Patrón | Estado |
|--------|--------|
| State (alarma) | ✅ Implementado |
| Composite (acciones) | ✅ Implementado (híbrido: enum persiste, Factory reconstruye en memoria) |
| Observer (notificaciones) | ✅ Implementado (Firebase simulado por log) |
| Scheduler (disparo alarmas) | ✅ Implementado |
| Strategy (exportación) | ❌ Pendiente |
| Decorator (exportación) | ❌ Pendiente |
| Factory (exportación) | ❌ Pendiente |
| Strategy (recordatorios) | ❌ Pendiente |
| Factory (recordatorios) | ❌ Pendiente |
| Scheduler (recordatorios) | ❌ Pendiente |
| Adapter (auth) | ❌ Stub |

**En síntesis:** el proyecto está funcionalmente muy avanzado — todos los patrones estructurales y de comportamiento pedidos para el dominio (State, Composite, Observer) están implementados. Lo que falta son los patrones de infraestructura (exportación y recordatorios).

---

## 15. Patrones de Diseño — Diseño Objetivo (PDS-TP.mdj)
> Esta sección refleja el **diagrama UML actualizado** (`SDD-output/PDS-TP.mdj`). Es el **diseño objetivo**: en varios casos el código actual todavía usa una versión más simple — cada divergencia se marca con ⚠️. Se omiten elementos de scratch del diagrama (`Class1-4`, `Interface1-5`, `IAlarma` duplicado, etc.).

### 15.1 State — Estado de la Alarma
- **Participantes:** interfaz `IEstadoAlarma`; estados concretos `EstadoActiva`, `EstadoAtendida`, `EstadoFinalizada`. `Alarma.estado` es de tipo `IEstadoAlarma`.
- **Intención:** cada estado encapsula su transición válida (`atender()`) y si bloquea la adopción (`bloqueaAdopcion()`). `AtencionAlarmaService` llama `alarma.getEstado().atender(alarma, tratamientoFinalizado)` y el estado actual decide el siguiente.
- **Persistencia:** `EstadoAlarmaConverter` (`@Converter`) serializa a `"ACTIVA"/"ATENDIDA"/"FINALIZADA"` en BD y reconstruye la instancia de estado al cargar.
- ✅ **Implementado.**

### 15.2 Composite — Acciones de la alarma
- **Participantes:** componente `ComponenteAccion`; compuesto `AccionComposite`; hojas `ControlParasitosAccion`, `ColocarAntiparasitarios`, `ComprobarPesoTamanio`, `ChequearNutricion`, `ColocarVacuna`. `AccionFactory` traduce enum → árbol.
- **Intención:** tratar acciones individuales y compuestas de forma uniforme. `getDescripcion()` recorre el árbol para generar la descripción del `RegistroAtencion`.
- **Persistencia:** el enum `AccionAlarma` + `@ElementCollection` sigue siendo la fuente de verdad en BD; el árbol Composite se reconstruye en memoria vía `AccionFactory.crearComposite()`. Ver [TD-02](#td-02--accionalarma-enum-como-persistencia--composite-en-memoria-v15).
- ✅ **Implementado** (enfoque híbrido).

### 15.3 Exportación: Strategy + Decorator + Factory
- **Strategy:** `IExportadorStrategy` (`exportarFicha(...)`) con `ExportadorPDF` y `ExportadorExcel` (`..|> IExportadorStrategy`). `FichaMedica` se asocia a `IExportadorStrategy`.
- **Decorator:** `ExportadorDecorator` envuelve un `IExportadorStrategy` (asociación `ExportadorDecorator <-> IExportadorStrategy` + método `exportadorStrategy(): IExportadorStrategy`); decoradores concretos `EncriptarDecorator` y `MarcaAguaDecorator`. Permiten **encriptar** y/o agregar **marca de agua** sin tocar los exportadores base ni multiplicar clases por combinación.
- **Factory:** `ExportadorFactory.crearExportador(formato: String, encriptar: boolean, agua: boolean): IExportadorStrategy` arma la cadena: instancia el exportador base por formato y lo envuelve con los decoradores pedidos (`ExportadorFactory ..> IExportadorStrategy`).
- **Impacto funcional:** introduce un **nuevo requerimiento** — exportar con encriptado y/o marca de agua opcionales (ver RN-15 / CU-02 en la spec funcional). El endpoint de exportación recibirá los flags `encriptar` y `agua`.
- ⚠️ **Divergencia:** hoy `ExportadorPDF`/`ExportadorExcel` son stubs; `ExportadorDecorator`, `EncriptarDecorator`, `MarcaAguaDecorator` y `ExportadorFactory` **no existen** en el código.

### 15.4 Factory — Recordatorios
- **Participantes:** `IRecordatorioFactory` / `RecordatorioFactory` crean la `IRecordatorioStrategy` adecuada según el canal (`SMSSender`, `WhatsAppSender`, `EmailSender`, todos `..|> IRecordatorioStrategy`), mapeado desde `PreferenciaRecordatorio`. `RecordatorioFactory ..> IRecordatorioStrategy`.
- **Intención:** centralizar la selección del canal; `VisitaSeguimientoService` pide al factory la estrategia y delega el envío.
- ⚠️ **Divergencia:** los senders son stubs; el factory **no existe** en el código.

### 15.5 Observer — Notificación de alarmas a veterinarios
- **Participantes:** `IObservado` (sujeto), `IObservador` (observador), `INotificadorPushObservador` (extiende `IObservador`). `Alerta` implementa `IObservado`. `Veterinario` implementa `IObservador`. `FirebasePushNotification` implementa `INotificadorPushObservador`.
- **Flujo:** `GestionAlarmaService.generarAlerta()` persiste la `Alerta`, suscribe todos los veterinarios + el canal push, y llama `alerta.alertarVeterinarios()` que itera los observadores y llama `actualizar(alerta)` en cada uno. `FirebasePushNotification` loguea la notificación (simulado).
- ✅ **Implementado.** (`INotificadorPush` eliminado — reemplazado por `INotificadorPushObservador`).

### 15.6 Adapter — Autenticación
- `AuthServiceAdapter` (Infraestructura) adapta el módulo externo de autenticación. Ya documentado en [TD-03](#td-03--referencia-a-usuario-externo-por-externalid); sigue como stub en el código.

### Resumen de patrones

| Patrón | Aplicado a | Clases clave | Estado en código |
|--------|-----------|--------------|-----------------|
| State | Estado de alarma | `IEstadoAlarma`, `EstadoActiva/Atendida/Finalizada`, `EstadoAlarmaConverter` | ✅ Implementado |
| Composite | Acciones de alarma | `ComponenteAccion`, `AccionComposite`, hojas, `AccionFactory` | ✅ Implementado (híbrido) |
| Observer | Alerta → veterinarios | `IObservado`, `IObservador`, `INotificadorPushObservador`, `FirebasePushNotification` | ✅ Implementado (log simulado) |
| Strategy | Exportación / Recordatorios | `IExportadorStrategy`, `IRecordatorioStrategy` | ❌ Stubs |
| Decorator | Exportación | `ExportadorDecorator`, `EncriptarDecorator`, `MarcaAguaDecorator` | ❌ No existe |
| Factory | Exportación / Recordatorios | `ExportadorFactory`, `RecordatorioFactory` | ❌ No existe |
| Adapter | Auth externo | `AuthServiceAdapter` | ❌ Stub |
