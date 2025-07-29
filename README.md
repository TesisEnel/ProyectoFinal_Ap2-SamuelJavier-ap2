
# ✨ Proyecto Final - EasySign🚀

Bienvenido al repositorio oficial del **Proyecto Final AP2**, una aplicación móvil desarrollada en **Android Studio con Jetpack Compose**, respaldada por **Firebase**. Esta app está diseñada para ofrecer una solución moderna y eficiente en la **gestión de pedidos personalizados de letreros**, permitiendo a los **clientes** crear pedidos y a los **administradores** gestionarlos con total control.

---

## 📱 Características Principales

### 🔒 Autenticación de Usuarios
- Registro e inicio de sesión con **Firebase Auth**.
- Roles definidos: `cliente` y `admin`.
- Persistencia de sesión activa incluso al cerrar la app.

### 🛠 Funcionalidades para Administradores
- 📋 Ver todos los pedidos y clasificarlos por estado: `pendiente`, `aceptado`, `rechazado`, `listo`.
- ✅ Cambiar el estado de un pedido y marcarlo como listo.
- 🧱 Crear, editar y eliminar **materiales** con descripción, imagen y precio.
- 🎨 Crear, editar y eliminar **letreros**.
- 👥 Ver listado de usuarios registrados (con foto de perfil).

### 🧾 Funcionalidades para Clientes
- 🧾 Crear pedidos personalizados con selección de material, tamaño (pequeño, mediano, grande o personalizado).
- 🖼 Subir imagen de referencia (como logo o diseño).
- 🧮 Cálculo automático del precio basado en el material y medidas.
- 📌 Visualizar estado del pedido en tiempo real.
- 🧾 Catálogo de materiales y letreros.
- 🧍 Ver perfil personal y editar datos.

### 🔔 Notificaciones Push
- Envío de notificaciones al admin cuando un cliente realiza un pedido.
- Notificaciones al cliente cuando el estado del pedido cambia.

---

## 🧠 Arquitectura del Proyecto

- **Jetpack Compose** para la UI moderna y declarativa.
- **MVVM** para separación de responsabilidades.
- **Hilt** para inyección de dependencias.
- **Firebase Firestore** como base de datos en tiempo real.
- **Firebase Storage** para imágenes.
- **Firebase Auth** para login y registro.

---

## 💻 Tecnologías Usadas

| Tecnología | Uso |
|------------|-----|
| Jetpack Compose | Interfaz moderna y eficiente |
| Firebase Auth | Registro y autenticación de usuarios |
| Firebase Firestore | Base de datos para usuarios, pedidos, materiales |
| Firebase Storage | Subida y visualización de imágenes |
| Firebase Cloud Messaging | Notificaciones push |
| Hilt | Inyección de dependencias |
| Coil | Carga de imágenes en Compose |
| Kotlin | Lenguaje principal de desarrollo |

---
