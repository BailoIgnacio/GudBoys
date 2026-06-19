# Functional Spec — Gud Boys (Refugio Animal)
> Version: 1.0 | Fecha: 2026-06-19 | Estado: Draft

## 1. Propósito y Resumen

**Gud Boy** es una cadena de refugios animales que necesita un sistema para:
- Gestionar el ingreso y seguimiento médico de animales (domésticos y salvajes)
- Administrar alarmas de control veterinario y tratamientos
- Manejar el proceso de adopción de animales domésticos
- Coordinar visitas de seguimiento post-adopción

El sistema interactúa con un módulo externo de autenticación (no gestionado por este equipo) y con servicios de notificación (push, SMS, WhatsApp, email).

---

## 2. Usuarios y Actores

| Actor | Descripción |
|-------|-------------|
| **Veterinario** | Crea alarmas, atiende alertas, completa fichas médicas |
| **Visitador** | Realiza visitas domiciliarias post-adopción, completa encuestas |
| **Sistema de Autenticación (externo)** | Gestiona login/registro de usuarios. Nuestro sistema solo guarda una referencia (`externalId`) |
| **Adoptante** | Cliente que adopta un animal doméstico (no tiene acceso al sistema en sí) |
| **Sistema de Notificaciones (externo)** | Firebase (push), SMS, WhatsApp, Email |

---

## 3. Casos de Uso

### CU-01 — Ingresar Animal al Refugio
**Actor:** Veterinario  
**Descripción:** Se registra un animal nuevo con su ficha técnica y médica inicial.

**Flujo:**
1. El veterinario carga los datos del animal: tipo (doméstico/salvaje), especie, altura, peso, edad aproximada, condición médica.
2. El sistema crea la entidad `Animal` correspondiente (`AnimalDomestico` o `AnimalSalvaje`).
3. El sistema genera automáticamente una `FichaMedica` vacía asociada al animal.
4. El sistema persiste el animal y devuelve confirmación.

**Reglas de negocio:** RN-01, RN-02

---

### CU-02 — Exportar Ficha Médica
**Actor:** Veterinario  
**Descripción:** Se exporta la ficha médica de un animal en PDF o Excel.

**Flujo:**
1. El veterinario selecciona un animal y el formato de exportación (PDF o Excel).
2. El sistema delega la exportación al exportador correspondiente (`ExportadorPDF` o `ExportadorExcel`).
3. El sistema retorna el archivo exportado.

**Reglas de negocio:** RN-03  
**Nota:** El diseño debe permitir agregar nuevos formatos sin modificar la lógica existente [INFERRED → Strategy pattern].

---

### CU-03 — Crear/Actualizar Alarma de Control
**Actor:** Veterinario  
**Descripción:** Se programa una alarma periódica de control para un animal.

**Flujo:**
1. El veterinario selecciona un animal y configura una alarma.
2. Configura: periodicidad (en días), si es tratamiento médico activo, y las acciones a ejecutar (de entre las 5 disponibles).
3. El sistema guarda la alarma asociada al animal con estado `ACTIVA`.

**Reglas de negocio:** RN-04, RN-07  
**Acciones posibles:** Control de parásitos, Colocar antiparasitarios, Comprobar peso y tamaño, Chequear nutrición, Colocar vacuna.

---

### CU-04 — Disparar y Atender Alarma
**Actor:** Sistema (disparo automático), Veterinario (atención)  
**Descripción:** Al llegar el momento de una alarma, el sistema genera una alerta para todos los veterinarios.

**Flujo:**
1. El sistema detecta que una alarma está vencida y la dispara.
2. Se genera una `Alerta` y se envía push notification a **todos** los veterinarios [Observer].
3. Cualquier veterinario puede atender la alarma.
4. El veterinario marca las acciones como completadas con un comentario.
5. Si es tratamiento médico, indica si finalizó o no.
6. Se crea un `RegistroAtencion` (evento en la ficha médica).
7. La alarma pasa a estado `ATENDIDA`.
8. Si el tratamiento finalizó, la alarma pasa a `FINALIZADA` y el animal queda libre para adopción.

**Reglas de negocio:** RN-04, RN-05, RN-06, RN-07

---

### CU-05 — Registrar Adopción
**Actor:** Veterinario / Empleado del refugio  
**Descripción:** Se registra la adopción de un animal doméstico.

**Flujo:**
1. Se cargan los datos del adoptante (nombre, apellido, estado civil, email, teléfono, ocupación, si tiene mascotas, motivo, tipos de animales interesados).
2. Se selecciona el animal doméstico a adoptar.
3. El sistema valida que el animal sea adoptable (RN-08, RN-09).
4. Se completan los papeles de adopción.
5. Se crea el registro de `Adopcion`.

**Reglas de negocio:** RN-08, RN-09, RN-10

---

### CU-06 — Configurar Seguimiento de Visitas
**Actor:** Veterinario / Empleado  
**Descripción:** Post-adopción, se configura el seguimiento de visitas domiciliarias.

**Flujo:**
1. Se asocia un `Visitador` responsable del seguimiento.
2. Se define la cadencia de visitas (día y rango horario).
3. Se configura la preferencia de recordatorio (SMS, WhatsApp o Email).
4. El sistema guarda el `SeguimientoVisitas`.

**Reglas de negocio:** RN-11, RN-12

---

### CU-07 — Registrar Visita Domiciliaria
**Actor:** Visitador  
**Descripción:** El visitador realiza la visita y registra los resultados.

**Flujo:**
1. El visitador completa la encuesta de seguimiento (estado del animal, limpieza del lugar, ambiente).
2. Indica si se deben continuar las visitas o no.
3. Se crea un registro de `VisitaDomicilio` con la `EncuestaSeguimiento` y se agrega al historial del animal (ficha médica).

**Reglas de negocio:** RN-13, RN-14

---

### CU-08 — Enviar Recordatorio de Visita
**Actor:** Sistema (automático)  
**Descripción:** El sistema envía recordatorios 'N' días antes de cada visita.

**Flujo:**
1. El sistema calcula las visitas próximas dentro de los próximos 'N' días (configurable por parámetro).
2. Para cada visita próxima, envía recordatorio al adoptante y al visitador según la preferencia elegida (SMS, WhatsApp o Email).

**Reglas de negocio:** RN-12  
**Nota:** El canal de notificación es seleccionable por Strategy [IRecordatorioStrategy].

---

## 4. Reglas de Negocio

### RN-01 — Tipos de Animal
Los animales se clasifican en **Doméstico** (perro, gato, canario, loro, tortuga, etc.) o **Salvaje** (zorro, pingüino, halcón, etc.). Esta clasificación determina si pueden ser adoptados.  
*Referenciado por: CU-01, CU-05*

### RN-02 — Ficha Técnica Obligatoria
Todo animal que ingresa al refugio debe tener altura, peso, edad aproximada y condición médica inicial.  
*Referenciado por: CU-01*

### RN-03 — Escalabilidad en Exportación
El sistema debe soportar la exportación de fichas médicas a PDF y Excel, con capacidad de agregar nuevos formatos sin modificar código existente.  
*Referenciado por: CU-02*

### RN-04 — Alarma con Acciones
Cada alarma tiene una periodicidad (en días) y un conjunto de acciones a ejecutar. Las acciones posibles son: Control de parásitos, Colocar antiparasitarios, Comprobar peso y tamaño, Chequear nutrición, Colocar vacuna.  
*Referenciado por: CU-03, CU-04*

### RN-05 — Notificación a Veterinarios
Cuando se dispara una alarma, se envía una push notification a **todos** los veterinarios del sistema.  
*Referenciado por: CU-04*

### RN-06 — Registro de Atención
Cuando un veterinario atiende una alarma, debe dejar un comentario/registro de lo realizado. Si es tratamiento médico, debe indicar si finalizó.  
*Referenciado por: CU-04*

### RN-07 — Estado del Tratamiento y Adopción
Un animal bajo tratamiento médico activo (alarma de tipo tratamiento en estado ACTIVA o ATENDIDA sin finalizar) **no puede ser adoptado**. Solo puede adoptarse cuando el tratamiento finaliza.  
*Referenciado por: CU-04, CU-05*

### RN-08 — Solo Domésticos son Adoptables
Los animales salvajes **nunca** pueden ser adoptados, independientemente de su estado médico.  
*Referenciado por: CU-05*

### RN-09 — Límite de Adopciones por Adoptante
Cada adoptante puede adoptar un **máximo de 2 animales** domésticos.  
*Referenciado por: CU-05*

### RN-10 — Datos del Adoptante
Al registrar una adopción se requiere: nombre, apellido, estado civil, email, teléfono, ocupación (empleado/estudiante/otros), si tiene otras mascotas, motivo de adopción y tipos de animales de interés.  
*Referenciado por: CU-05*

### RN-11 — Asignación de Visitador
Todo seguimiento post-adopción debe tener un visitador responsable asignado.  
*Referenciado por: CU-06*

### RN-12 — Recordatorio Configurable
El sistema envía recordatorios 'N' días antes de cada visita (N es configurable por parámetro en application.properties). Los canales disponibles son SMS, WhatsApp y Email.  
*Referenciado por: CU-06, CU-08*

### RN-13 — Encuesta de Visita
Cada visita domiciliaria incluye una encuesta con tres ítems calificados como MALO/REGULAR/BUENO: estado general del animal, limpieza del lugar, ambiente.  
*Referenciado por: CU-07*

### RN-14 — Historial Unificado
Todos los eventos del animal (registros de atención veterinaria y visitas domiciliarias) quedan en el historial de la ficha médica del animal, enlazados cronológicamente.  
*Referenciado por: CU-04, CU-07*

---

## 5. Flujos Principales

```mermaid
sequenceDiagram
    participant V as Veterinario
    participant S as Sistema
    participant N as Push Notifications
    participant VT as Todos los Veterinarios

    V->>S: POST /api/animales/{id}/alarmas (CrearAlarmaRequest)
    S-->>V: AlarmaResponse (estado: ACTIVA)
    Note over S: Periodicidad transcurrida...
    S->>S: Disparar Alarma
    S->>N: Enviar push notification
    N->>VT: Alerta disparada
    V->>S: POST /api/alarmas/{id}/atender (AtenderAlarmaRequest)
    S->>S: Crear RegistroAtencion en FichaMedica
    S-->>V: AlarmaResponse (estado: ATENDIDA o FINALIZADA)
```

```mermaid
sequenceDiagram
    participant E as Empleado
    participant S as Sistema
    participant Ad as Adoptante

    E->>S: POST /api/adopciones (RegistrarAdopcionRequest)
    S->>S: Validar animal adoptable (RN-07, RN-08, RN-09)
    S-->>E: AdopcionResponse
    E->>S: POST /api/seguimientos (ConfigurarSeguimientoRequest)
    S-->>E: SeguimientoResponse
    Note over S: N días antes de la visita...
    S->>Ad: Recordatorio por canal preferido (SMS/WhatsApp/Email)
```

---

## 6. Estados de la Alarma

| Estado | Descripción | Transición |
|--------|-------------|------------|
| `ACTIVA` | Alarma programada y vigente. Bloquea adopción si es tratamiento. | → ATENDIDA (al ser atendida) |
| `ATENDIDA` | Fue atendida pero el tratamiento no finalizó. Sigue bloqueando adopción. | → FINALIZADA (al indicar fin de tratamiento) |
| `FINALIZADA` | Tratamiento completo o control sin tratamiento. No bloquea adopción. | (estado terminal) |

---

## 7. Fuera de Alcance

- Login y registro de usuarios (responsabilidad del módulo de autenticación externo).
- Gestión de permisos y roles (responsabilidad del módulo de seguridad externo).
- Pasarela de pagos u honorarios de adopción.
- Gestión de inventario de medicamentos o insumos.
- Aplicación mobile (solo backend/API REST).

---

## 8. Glosario del Dominio

| Término | Definición |
|---------|------------|
| **Animal Doméstico** | Animal que puede ser adoptado (perro, gato, canario, loro, tortuga, etc.) |
| **Animal Salvaje** | Animal rescatado pero no adoptable (zorro, pingüino, halcón, etc.) |
| **Ficha Médica** | Historial médico completo del animal: eventos, tratamientos, visitas |
| **Alarma** | Control periódico programado por un veterinario para un animal |
| **Alerta** | Notificación generada cuando una alarma se dispara |
| **RegistroAtencion** | Evento en la ficha médica que documenta la atención de una alarma |
| **Adoptante** | Cliente externo que adopta un animal |
| **SeguimientoVisitas** | Configuración del seguimiento post-adopción |
| **VisitaDomicilio** | Una visita concreta al hogar del adoptante |
| **EncuestaSeguimiento** | Evaluación de la visita (estado animal, limpieza, ambiente) |
| **ExternalId** | Referencia al usuario en el sistema de autenticación externo |

---

## 9. Supuestos a Validar

- [ ] ¿El disparo de alarmas es automático (job scheduler) o manual (el veterinario dispara)?
- [ ] ¿El `externalId` del usuario es un UUID o un entero? ¿Qué formato usa el módulo de autenticación?
- [ ] ¿El adoptante recibe acceso al sistema o solo es un registro de datos?
- [ ] ¿El parámetro 'N' de recordatorios es global o configurable por adopción?
- [ ] ¿Puede un veterinario crear múltiples alarmas para el mismo animal simultáneamente?
- [ ] ¿Las visitas domiciliarias tienen una fecha programada de antemano o son flexibles?
