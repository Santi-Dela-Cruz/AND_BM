# Biosequence Pattern Search Tool (JavaFX)

Este proyecto implementa una herramienta gráfica interactiva para buscar patrones específicos dentro de secuencias de ADN, utilizando el algoritmo de Boyer-Moore.

## Requisitos

- Java JDK 17 o superior
- JavaFX SDK (no incluido en JDK)
- IntelliJ IDEA (preferido)

## Instrucciones para ejecutar el sistema en IntelliJ IDEA

1. **Descargar JavaFX SDK**
   - Ir a: https://gluonhq.com/products/javafx/
   - Descargar JavaFX SDK para Windows
   - Extraerlo en una carpeta, por ejemplo:
     ```
     C:\Java\javafx-sdk-24.0.1
     ```

2. **Abrir el proyecto en IntelliJ**
   - Abrir IntelliJ IDEA
   - Seleccionar `File > Open` y elegir la carpeta raíz del proyecto

3. **Crear configuración de ejecución**
   - Ir a `Run > Edit Configurations...`
   - Clic en el botón `+` > **Application**
   - En `Name`: `RunApp`
   - En `Main class`: `application.App`
   - En `Use classpath of module`: selecciona el módulo del proyecto

4. **Agregar VM Options**
   - Clic en `Modify Options` > activar `Add VM options`
   - En el campo, pegar (ajustando la ruta):
     ```
     --module-path C:\Java\javafx-sdk-24.0.1\lib --add-modules javafx.controls,javafx.fxml
     ```

5. **Ejecutar**
   - Haz clic en `Apply`, luego `OK`
   - Ejecutar con `Shift + F10` o el botón ▶️

# Biosequence Pattern Search Tool (JavaFX)

Este proyecto implementa una herramienta gráfica interactiva para buscar patrones específicos dentro de secuencias de ADN, utilizando el algoritmo de Boyer-Moore.

## Requisitos

- Java JDK 17 o superior
- JavaFX SDK (no incluido en JDK)
- IntelliJ IDEA (preferido)

## Instrucciones para ejecutar el sistema en IntelliJ IDEA

1. **Descargar JavaFX SDK**
   - Ir a: https://gluonhq.com/products/javafx/
   - Descargar JavaFX SDK para Windows
   - Extraerlo en una carpeta, por ejemplo:
     ```
     C:\Java\javafx-sdk-24.0.1
     ```

2. **Abrir el proyecto en IntelliJ**
   - Abrir IntelliJ IDEA
   - Seleccionar `File > Open` y elegir la carpeta raíz del proyecto

3. **Crear configuración de ejecución**
   - Ir a `Run > Edit Configurations...`
   - Clic en el botón `+` > **Application**
   - En `Name`: `RunApp`
   - En `Main class`: `application.App`
   - En `Use classpath of module`: selecciona el módulo del proyecto

4. **Agregar VM Options**
   - Clic en `Modify Options` > activar `Add VM options`
   - En el campo, pegar (ajustando la ruta):
     ```
     --module-path C:\Java\javafx-sdk-24.0.1\lib --add-modules javafx.controls,javafx.fxml
     ```

5. **Ejecutar**
   - Haz clic en `Apply`, luego `OK`
   - Ejecutar con `Shift + F10` o el botón ▶️

