No hecho (todo lo marcado [TODO] en la sección 12 del technical-spec, confirmado)

- Toda la lógica de negocio: los 5 services (IngresoAnimalService, GestionAlarmaService, AtencionAlarmaService, AdopcionService, VisitaSeguimientoService) solo tiran UnsupportedOperationException("Not implemented yet") en cada
  método.
- Exportación PDF/Excel: stubs, y ni siquiera están las dependencias (iText/POI) en el pom.
- Notificaciones (Firebase push, SMS, WhatsApp, Email) y AuthServiceAdapter: todos stubs.
- Scheduler para disparo automático de alarmas y recordatorios de visitas: no existe ningún @Scheduled en todo el código.
- Manejo global de excepciones (@ControllerAdvice): no existe.
- Seguridad/JWT: no existe.
- Tests: carpeta src/test/java vacía, cero tests.
- Flyway/Liquibase: no están (la spec lo marca como asunción de "no requerido").
