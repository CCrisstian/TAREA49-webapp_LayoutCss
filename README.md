<h1 aling="center">Tarea: Agregar layout y estilos Bootstrap CSS</h1>
<h2>Instrucciones de tareas</h2>
<p>El objetivo de la tarea consiste en modificar la tarea anterior para agregar layout y estilos Bootstrap CSS en el listado y formulario.</p>
<p>Una vez terminada y probada la tarea deberán enviar el código fuente de todos los archivos involucrados, además de las imágenes screenshot (imprimir pantalla).</p>

- Vistas header.jsp y footer.jsp.
- Vistas listar.jsp y form.jsp.

<p>Debe quedar de la siguiente forma:</p>

<p align="center"><img width="719" alt="image" src="https://github.com/user-attachments/assets/338a52fa-3197-4fff-9c0e-b2d908db34a8"></p>
<p align="center"><img width="719" alt="image" src="https://github.com/user-attachments/assets/667f832c-3d72-4362-b983-e6a52088003c"></p>

<h1>Resolución del Profesor</h1>

- header.jsp
```jsp
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${titulo}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
  <div class="container-fluid">
    <a class="navbar-brand" href="#">Cursos</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/cursos">Listado</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/cursos/form">Crear Curso</a>
        </li>
      </ul>

    </div>
  </div>
</nav>
<div class="container">
```
- footer.jsp
```jsp
</div>
</body>
</html>
```
- listar.jsp
```jsp
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.time.format.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="layout/header.jsp" />
<h1>${titulo}</h1>
<p><a href="${pageContext.request.contextPath}/cursos/form" class="btn btn-primary my-2">crear [+]</a></p>
<form action="${pageContext.request.contextPath}/cursos/buscar" method="post">
    <div class="row mb-4">
        <div class="col-sm-4">
            <input type="text" name="nombre" class="form-control">
        </div>
        <div class="col-sm-2">
            <input type="submit" value="Buscar" class="btn btn-light">
        </div>
    </div>
</form>
<table class="table table-hover table-striped">
    <tr>
        <th>id</th>
        <th>nombre</th>
        <th>instructor</th>
        <th>duracion</th>
        <th>editar</th>
        <th>eliminar</th>
    </tr>
    <c:forEach items="${cursos}" var="c">
        <tr>
            <td>${c.id}</td>
            <td>${c.nombre}</td>
            <td>${c.instructor}</td>
            <td>${c.duracion}</td>
            <td><a href="${pageContext.request.contextPath}/cursos/form?id=${c.id}" class="btn btn-sm btn-success">editar</a></td>
            <td><a onclick="return confirm('esta seguro que desea eliminar?');"
                   href="${pageContext.request.contextPath}/cursos/eliminar?id=${c.id}" class="btn btn-sm btn-danger">eliminar</a></td>
        </tr>
    </c:forEach>
</table>
<jsp:include page="layout/footer.jsp" />
```
- form.jsp:
```jsp
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.time.format.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="layout/header.jsp" />
<h1>${titulo}</h1>
<p><a href="${pageContext.request.contextPath}/cursos" class="btn btn-light">volver</a></p>
<form action="${pageContext.request.contextPath}/cursos/form" method="post">
    <div class="row mb-2">
        <label for="nombre" class="col-form-label col-sm-2">Nombre</label>
        <div class="col-sm-4">
            <input type="text" name="nombre" id="nombre" value="${curso.nombre}" class="form-control">
        </div>
        <c:if test="${errores != null && errores.containsKey('nombre')}">
            <div style="color:red;">${errores.nombre}</div>
        </c:if>
    </div>
    <div class="row mb-2">
        <label for="instructor" class="col-form-label col-sm-2">Instructor</label>
        <div class="col-sm-4">
            <input type="text" name="instructor" id="instructor" value="${curso.instructor}" class="form-control">
        </div>
        <c:if test="${errores != null && errores.containsKey('instructor')}">
            <div style="color:red;">${errores.instructor}</div>
        </c:if>
    </div>
    <div class="row mb-2">
        <label for="duracion" class="col-form-label col-sm-2">Duracion</label>
        <div class="col-sm-4">
            <input type="text" name="duracion" id="duracion" value="${curso.duracion != null && curso.duracion > 0? curso.duracion: ""}" class="form-control">
        </div>
        <c:if test="${errores != null && errores.containsKey('duracion')}">
            <div style="color:red;">${errores.duracion}</div>
        </c:if>
    </div>
    <div class="row mb-2">
        <label for="descripcion" class="col-form-label col-sm-2">Descripción</label>
        <div class="col-sm-4">
            <textarea name="descripcion" id="descripcion" class="form-control">${curso.descripcion}</textarea>
        </div>
        <c:if test="${errores != null && errores.containsKey('descripcion')}">
            <div style="color:red;">${errores.descripcion}</div>
        </c:if>
    </div>
    <div class="row mb-6">
        <div><input type="submit" value="${curso.id!=null && curso.id>0? "Editar": "Crear"}" class="btn btn-primary"></div>
    </div>
    <input type="hidden" name="id" value="${curso.id}">
</form>
<jsp:include page="layout/footer.jsp" />
```
