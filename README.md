Lo que se creó

SDD-output/ (abrir sdd-viewer.html en el browser)
- functional-spec.md — casos de uso, reglas de negocio, flujos, estados
- technical-spec.md — modelo de datos, API surface, decisiones técnicas, gaps
- sdd-viewer.html — viewer interactivo con navegación y diagramas Mermaid

GudBoysCode/ — 69 archivos Java + config:
- pom.xml — Spring Boot 3.3, JPA, MySQL, Lombok, Validation
- application.properties + application-test.properties (H2 para tests)
- 5 enums: AccionAlarma, EstadoAlarma, Calificacion, Ocupacion, PreferenciaRecordatorio
- 12 entidades JPA: Animal, AnimalDomestico, AnimalSalvaje, FichaMedica, Evento, RegistroAtencion, Alarma, Alerta,
  Usuario, Veterinario, Visitador, Adoptante, Adopcion, SeguimientoVisitas, VisitaDomicilio, EncuestaSeguimiento
- 6 DTOs request + 7 DTOs response con validaciones Jakarta
- 4 mappers con conversión completa entity ↔ DTO
- 8 repositories Spring Data JPA con queries custom
- 5 interfaces de service + 5 implementations vacías (con TODO comentados)
- 4 controllers REST completamente wired
- Infraestructura: IExportadorStrategy + PDF/Excel, IRecordatorioStrategy + SMS/WA/Email, INotificadorPush + Firebase,
  AuthServiceAdapter

  ---
Mis recomendaciones para el TP

1. Strategy para exportación → OBLIGATORIO implementarlo. El enunciado lo pide explícitamente. ExportadorPDF y
   ExportadorExcel ya están creados vacíos.
2. Observer para push notifications → OBLIGATORIO. Cuando AtencionAlarmaService atiende una alarma, debe llamar a
   FirebasePushNotification.notificar() con todos los veterinarios.
3. Strategy para recordatorios → OBLIGATORIO. En VisitaSeguimientoService.enviarRecordatoriosProximos(), seleccionar
   el IRecordatorioStrategy según la preferenciaRecordatorio de cada seguimiento.
4. State para Alarma → Recomendado pero no urgente. Ya manejás los estados con el enum EstadoAlarma. Si el docente
   pide el patrón formal, la lógica de transición va en el AtencionAlarmaService.
5. Composite para acciones → No lo implementes. El List<AccionAlarma> ya es suficiente y más claro para este caso.