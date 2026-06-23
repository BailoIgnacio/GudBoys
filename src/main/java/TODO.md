# Workflow de cierre del proyecto — Gud Boys

> Plan de tareas para terminar el TP, dividido en **2 tracks paralelos** (Persona A / Persona B)
> pensados para minimizar conflictos de merge. Detalle de diseño de cada patrón en
> `SDD-output/technical-spec.md` §15 y plan general en §14.
>
> Convención de owners: **[A]** Track Alarmas · **[B]** Track Exportación+Recordatorios · **[0]** Setup compartido.
> Marcá con `[x]` lo terminado. Cada tarea indica **archivos**, **qué hacer**, **criterio de aceptación (CA)** y **depende de**.

---

## Estado actual (punto de partida)

- ✅ Lógica de negocio de los 5 services (salvo `enviarRecordatoriosProximos`).
- ✅ Controllers Animal/Alarma/Adopcion/Seguimiento. Validaciones RN-02/04/06/07/08/09/10/11/13/14.
- ❌ **Pendiente = patrones de diseño + scheduler** (esto es lo que cubre este workflow).
- ⚠️ Divergencias código↔diagrama: hoy `Alarma.estado` es enum y `Alarma.acciones` es enum+ElementCollection; el diagrama pide **State** y **Composite**.

---

## FASE 0 — Setup compartido `[0]`

> Lo hace **una sola persona** y se **mergea a `main` ANTES** de arrancar los tracks (los dos dependen de esto). ~30-45 min.

- [ ] **0.1 Estrategia de ramas.** Crear rama por track: `feat/track-a-alarmas` y `feat/track-b-export-recordatorios`. PRs chicos contra `main`.
- [ ] **0.2 Habilitar scheduling.** En `GudBoysApplication.java` agregar `@EnableScheduling`.
  - **CA:** la app levanta sin error con la anotación puesta.
- [ ] **0.3 Excepciones de dominio + handler.** En `exceptions/`: crear excepciones base (`NotFoundException`, `BusinessException` o similar) y ampliar `GlobalExceptionHandler` para manejar `AnimalException` y un handler genérico (`Exception` → 500).
  - **CA:** un `findById` fallido devuelve 404 con `ErrorResponseDTO`; un error de negocio devuelve 400/409.
  - **Nota:** los services hoy tiran `RuntimeException` genérica; la migración fina de cada service a estas excepciones se hace dentro de cada track (tareas A.x / B.x), pero las clases base quedan acá.

---

## TRACK A — Alarmas: State + Composite + Observer + Scheduler `[A]`

> Owner: __________. Rama: `feat/track-a-alarmas`. Toca el dominio de alarma, notificación push y scheduler de alarmas.

### A.1 — Patrón State (estado de la alarma) `[A]`  · depende de: Fase 0

- [x] Crear paquete `domain/state/` con interfaz `IEstadoAlarma` y estados `EstadoActiva`, `EstadoAtendida`, `EstadoFinalizada`.
- [x] Cada estado encapsula la transición (`atender(...)`, `finalizar(...)`) y si **bloquea adopción** (`bloqueaAdopcion(): boolean`).
- [x] En `domain/Alarma.java`: cambiar `estado` a tipo State. Mantener una **columna discriminadora** persistible (conservar el enum `EstadoAlarma` como discriminador en BD y reconstruir el objeto State con `@PostLoad`/factory, o `@Convert`).
- [x] Ajustar `mapper/AlarmaMapper.java` (hoy setea `EstadoAlarma.ACTIVA`).
- [x] Ajustar `service/impl/AtencionAlarmaService.java`: reemplazar el `if/else` de transición por `alarma.getEstado().atender(...)`.
- [x] Ajustar `domain/AnimalDomestico.java` y `domain/FichaMedica.java`: `esAdoptable()` / `estaBajoTratamientoActivo()` deben consultar el State (`bloqueaAdopcion()`) en vez de comparar el enum.

- **CA:** atender una alarma de tratamiento la pasa a `ATENDIDA` y, si finalizó, a `FINALIZADA`; un animal con alarma activa de tratamiento no es adoptable. Sin romper los tests/endpoints existentes.

### A.2 — Patrón Composite (acciones de la alarma) `[A]`  · depende de: Fase 0

- [ ] Crear paquete `domain/accion/` con `ComponenteAccion` (interfaz), `AccionComposite` (`List<ComponenteAccion>`) y las 5 hojas: `ControlParasitosAccion`, `ColocarAntiparasitarios`, `ComprobarPesoTamanio`, `ChequearNutricion`, `ColocarVacuna`.
- [ ] Definir la operación común (p. ej. `descripcion()` / `ejecutar()`).
- [ ] Integrar en `domain/Alarma.java` y `domain/RegistroAtencion.java` (acciones realizadas).
- [ ] Ajustar `mapper/AlarmaMapper.java` y los DTOs `CrearAlarmaRequestDTO` / `AtenderAlarmaRequestDTO` (mapear strings/enums entrantes → componentes).

- **Nota de persistencia:** el árbol Composite con JPA es más complejo que el enum. Decidir: (a) persistir como `@ElementCollection` de un identificador y reconstruir el árbol, o (b) mapear la jerarquía. Documentar la decisión en el technical-spec (actualizar TD-02/§15.2).
- **CA:** se puede crear una alarma con varias acciones y leerlas de vuelta en el `AlarmaResponseDTO`.

### A.3 — Patrón Observer (alerta → veterinarios) `[A]`  · depende de: A.1

- [ ] Crear paquete `domain/observer/` con `IObservado`, `IObservador`, `INotificadorPushObservador` (extiende `IObservador`, realiza `IObservado`).
- [ ] `domain/Veterinario.java` implementa `IObservador`; `domain/Alerta.java` participa como observado (`alertarVeterinarios()`).
- [ ] Implementar `infrastructure/notification/FirebasePushNotification.notificar(...)` (real o simulado con log) como `INotificadorPushObservador`.
- [ ] En `service/impl/GestionAlarmaService.java`: implementar `generarAlerta()` — crea la `Alerta`, obtiene todos los veterinarios y los notifica.
- [ ] Repo `IAlertaRepository` para persistir la alerta.

- **CA:** disparar una alarma genera una `Alerta` persistida y registra (log/mock) el envío push a todos los veterinarios.

### A.4 — Scheduler de disparo de alarmas `[A]`  · depende de: A.3, Fase 0.2

- [ ] **Agregar campo de fecha a `Alarma`** (hoy NO existe): `fechaProximoDisparo` (o `fechaUltimoDisparo`), seteado al crear/atender según `periodicidad`.
- [ ] En `IAlarmaRepository` agregar query de alarmas vencidas (`findByEstadoAndFechaProximoDisparoBefore(...)`).
- [ ] Crear `scheduler/AlarmaScheduler.java` con `@Scheduled` que busca alarmas vencidas y llama a `generarAlerta()`.

- **CA:** una alarma cuya fecha de próximo disparo ya pasó es detectada por el job y dispara la alerta.

---

## TRACK B — Exportación + Recordatorios `[B]`

> Owner: __________. Rama: `feat/track-b-export-recordatorios`. Toca infraestructura de export/notificación, `pom.xml`, `AnimalController` y `VisitaSeguimientoService`.

### B.1 — Strategy de exportación (PDF/Excel) `[B]`  · depende de: Fase 0

- [ ] Agregar dependencias al `pom.xml`: iText o Apache PDFBox (PDF) y Apache POI (Excel).
- [ ] Implementar `infrastructure/export/ExportadorPDF.exportarFicha(...)` y `ExportadorExcel.exportarFicha(...)`.

- **CA:** exportar una ficha devuelve un `byte[]` válido de PDF y de Excel.

### B.2 — Decorator de exportación (encriptar / marca de agua → RN-15) `[B]`  · depende de: B.1

- [ ] Crear `infrastructure/export/ExportadorDecorator` (envuelve un `IExportadorStrategy`).
- [ ] Crear `EncriptarDecorator` y `MarcaAguaDecorator`.

- **CA:** se puede exportar un PDF encriptado, con marca de agua, ambos, o ninguno.

### B.3 — Factory de exportación `[B]`  · depende de: B.2

- [ ] Crear `infrastructure/export/ExportadorFactory.crearExportador(formato, encriptar, agua): IExportadorStrategy` que arma strategy + decoradores.

- **CA:** dado `(EXCEL, encriptar=true, agua=false)` devuelve el exportador correcto envuelto.

### B.4 — Endpoint de exportación `[B]`  · depende de: B.3

- [ ] En `controller/AnimalController.java` agregar `GET /api/animales/{id}/ficha-medica/exportar?formato=PDF|EXCEL&encriptar=&agua=` que devuelve el archivo binario.
- [ ] Obtener la `FichaMedica` (reusar `IngresoAnimalService`/repo) y pasarla al exportador del factory. **No modificar `FichaMedica.java`** (usarla read-only) para no chocar con Track A.

- **CA:** `GET .../exportar?formato=PDF` descarga el PDF con headers correctos (`Content-Type`, `Content-Disposition`).

### B.5 — Strategy + Factory de recordatorios `[B]`  · depende de: Fase 0

- [ ] Implementar los 3 senders `infrastructure/notification/SmsSender`/`WhatsAppSender`/`EmailSender` (`enviarRecordatorio(diasAntes)`), real o simulado con log.
- [ ] Crear `RecordatorioFactory` (`IRecordatorioFactory`) que elige la estrategia según `PreferenciaRecordatorio`.
- [ ] Implementar `service/impl/VisitaSeguimientoService.enviarRecordatoriosProximos()`: usar `ISeguimientoVisitasRepository.findVisitasEntreFechas(hoy, hoy+N)` (**ya existe**) y enviar por el canal preferido. `N` = `gudboys.recordatorio.dias-anticipacion`.

- **CA:** llamar `enviarRecordatoriosProximos()` con un seguimiento próximo registra (log/mock) el envío por el canal correcto.

### B.6 — Scheduler de recordatorios `[B]`  · depende de: B.5, Fase 0.2

- [ ] Crear `scheduler/RecordatorioScheduler.java` con `@Scheduled` (p. ej. diario) que invoca `enviarRecordatoriosProximos()`.

- **CA:** el job corre en el intervalo configurado y dispara los recordatorios próximos.

---

## FASE FINAL — Integración y robustez

> Después de mergear ambos tracks. Repartible.

- [ ] **F.1 [A+B]** Merge de ambos tracks a `main` y prueba de humo end-to-end (ingresar animal → alarma → atender → adoptar → seguimiento → visita → export).
- [ ] **F.2 [A]** Migrar `RuntimeException` genéricas de `AtencionAlarmaService`/`GestionAlarmaService` a las excepciones de Fase 0.3.
- [ ] **F.2 [B]** Migrar `RuntimeException` genéricas de `VisitaSeguimientoService`/`AdopcionService` a las excepciones de Fase 0.3.
- [ ] **F.3 [B]** `AuthServiceAdapter` real (Adapter) — solo si el módulo externo lo requiere.
- [ ] **F.4 [A+B]** Tests (JUnit 5 + Mockito + H2): ciclo de vida de alarma (A), validaciones de adopción salvaje/tratamiento/límite 2 (B), exportación (B).
- [ ] **F.5 [opcional]** Seguridad JWT, si entra en el alcance del TP.
- [ ] **F.6** Actualizar el SDD (technical-spec §15 y functional-spec) con las decisiones finales (sobre todo persistencia de Composite/State).

---

## Mapa de archivos por track (para evitar conflictos)


| Área                                      | Archivos                                                                                                                                                      | Owner                                 |
| ----------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------- |
| Dominio alarma + estados + acciones       | `domain/Alarma.java`, `domain/state/`*, `domain/accion/*`, `domain/enums/EstadoAlarma.java`, `domain/enums/AccionAlarma.java`, `domain/RegistroAtencion.java` | **A**                                 |
| Adopción/ficha (chequeo de estado)        | `domain/AnimalDomestico.java`, `domain/FichaMedica.java`                                                                                                      | **A** (B usa `FichaMedica` read-only) |
| Observer + push                           | `domain/observer/`*, `domain/Alerta.java`, `domain/Veterinario.java`, `infrastructure/notification/FirebasePushNotification.java`, `INotificadorPush.java`    | **A**                                 |
| Services de alarma + mapper               | `service/impl/AtencionAlarmaService.java`, `service/impl/GestionAlarmaService.java`, `mapper/AlarmaMapper.java`, `repository/IAlarmaRepository.java`          | **A**                                 |
| Scheduler alarmas                         | `scheduler/AlarmaScheduler.java`                                                                                                                              | **A**                                 |
| Exportación (Strategy/Decorator/Factory)  | `infrastructure/export/`*, `pom.xml`                                                                                                                          | **B**                                 |
| Endpoint export                           | `controller/AnimalController.java`                                                                                                                            | **B**                                 |
| Recordatorios (Strategy/Factory)          | `infrastructure/notification/SmsSender.java`, `WhatsAppSender.java`, `EmailSender.java`, `IRecordatorioStrategy.java`, `RecordatorioFactory.java`             | **B**                                 |
| Service visitas + scheduler recordatorios | `service/impl/VisitaSeguimientoService.java`, `scheduler/RecordatorioScheduler.java`                                                                          | **B**                                 |
| Compartidos (Fase 0, mergear primero)     | `GudBoysApplication.java`, `exceptions/`*                                                                                                                     | **0**                                 |


**Único punto de contacto entre tracks:** `domain/FichaMedica.java` (A lo edita por el State; B solo lo lee). Coordinar ese merge.