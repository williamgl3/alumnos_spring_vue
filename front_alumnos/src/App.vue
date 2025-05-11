<script setup>
import { ref, onMounted} from "vue";
import axios from 'axios';

const alumnos = ref([]);

const nuevoAlumno = ref({
  nombre: '',
  apellido:'',
  carrera:'',
  telefono:'',
  imagenURL:''
});

const cargarAlumnos = async () => {
  const response = await axios.get('http://localhost:8080/alumnos/traer-alumnos')
  alumnos.value = response.data;
  console.log(alumnos.value);
}

onMounted(cargarAlumnos);

const agregarAlumno = async () =>{
  await axios.post('http://localhost:8080/alumnos/insertar-alumnos', nuevoAlumno.value)
  await cargarAlumnos();
  nuevoAlumno.value = {
  nombre: '',
  apellido:'',
  carrera:'',
  telefono:'',
  imagenURL:''
  };
}

</script>

<template>
  <div class="container">
    <div class="row">
      <div class="col-md-12 mt-4">
        <div class="card shadow p-4 mb-4">
          <h2 class="text-center">Formulario de Alumnos</h2>
          <form @submit.prevent="agregarAlumno">
            <div class="row">
            <div class="col-md-6 mb-3">
              <label for="nombre" class="form-label">Nombre</label>
              <input type="text" class="form-control" id="nombre" v-model="nuevoAlumno.nombre" required>
            </div>
            <div class="col-md-6 mb-3">
              <label for="apellido" class="form-label">Apellidos</label>
              <input type="text" class="form-control" id="apellido" v-model="nuevoAlumno.apellido" required>
            </div>
            <div class="col-md-6 mb-3">
              <label for="carrera" class="form-label">Carrera</label>
              <input type="text" class="form-control" id="carrera" v-model="nuevoAlumno.carrera" required>
            </div>
            <div class="col-md-6 mb-3">
              <label for="telefono" class="form-label">Telefono</label>
              <input type="number" class="form-control" id="telefono" v-model="nuevoAlumno.telefono" required>
            </div>
            <div class="col-md-6 mb-3">
              <label for="imagenURL" class="form-label">imagenURL</label>
              <input type="text" class="form-control" id="imagenURL" v-model="nuevoAlumno.imagenURL">
            </div>
            </div>

            <button type="submit" class="btn btn-primary">Agregar Alumno</button>
          </form>

        </div>

      </div>
      <div class="col-md-12">
        <div class="card shadow">
          <div class="card-body">
            <h5 class="card-title mb-3">Tabla de Alumnos</h5>
             <table class="table table-hover align-middle">
  <thead>
    <tr>
      <th scope="col">Id</th>
      <th scope="col">Nombre</th>
      <th scope="col">Apellidos</th>
      <th scope="col">Carrera</th>
      <th scope="col">Telefono</th>
      <th scope="col">Imagen</th>
      <th scope="col">Acciones</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="alumno in alumnos" :key="alumno.id">
      <td>{{ alumno.id }}</td>
      <td>{{ alumno.nombre }}</td>
      <td>{{ alumno.apellido }}</td>
      <td>{{ alumno.carrera }}</td>
      <td>{{ alumno.telefono }}</td>
      <td><img :src="alumno.imagenURL" alt="Imagen del alumno" width="50"></td>
      <td>
        <button class="btn btn-danger mx-2"><i class="bi bi-trash3-fill"></i></button>
        <button class="btn btn-warning"><i class="bi bi-pencil-fill"></i></button>
      </td>
    </tr>
  </tbody>
</table>
          </div>
        </div>
       
   
      </div>
    </div>
    
  </div>
  
</template>

<style scoped></style>
