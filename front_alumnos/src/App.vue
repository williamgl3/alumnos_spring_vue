<script setup>
import { ref, onMounted} from "vue";
import axios from "axios";
import Swal from 'sweetalert2';

const motocicletas = ref([]);

const nuevaMotocicleta = ref({
  numeroSerie: '',
  marca: '',
  modelo: '',
  cilindrada: '',
  color: '',
  año: '',
  precio: '',
  imagenURL:''
});

const editado= ref(false);

const cargarMotocicletas = async () => {
  const response = await axios.get('http://localhost:8080/motocicletas/traer-motocicletas')
  motocicletas.value = response.data;
  console.log(motocicletas.value);
}

const agregarMotocicleta = async () =>{
  if (editado.value){
     await axios.put(`http://localhost:8080/motocicletas/editar-motocicletas/${nuevaMotocicleta.value.id}`, nuevaMotocicleta.value);
     editado.value = false
  }else{
     await axios.post('http://localhost:8080/motocicletas/insertar-motocicletas', nuevaMotocicleta.value)
     Swal.fire({
      icon: 'success',
      title: 'Motocicleta agregada correctamente',
      showConfirmButton: false,
      timer: 1500
     });
  }
 
  await cargarMotocicletas();
  nuevaMotocicleta.value = {
    numeroSerie: '',
    marca: '',
    modelo: '',
    cilindrada: '',
    color: '',
    año: '',
    precio: '',
    imagenURL:''
  };
}

const editarMotocicleta =(motocicleta) => {
  Object.assign(nuevaMotocicleta.value, motocicleta);
  editado.value = true
}

const eliminarMotocicleta = async (id) => {
  Swal.fire({
    title: '¿Estás seguro de eliminar esta motocicleta?',
    text: "No podrás revertir esto!",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Sí, eliminarla',
  }).then(async (result) => {
    if (result.isConfirmed) {
      await eliminarMotocicletaPorId(id);
      Swal.fire(
        'Eliminada!',
        'La motocicleta ha sido eliminada.',
        'success'
      )
    }
  })
}

const eliminarMotocicletaPorId = async (id) => {
  try{
    await axios.delete(`http://localhost:8080/motocicletas/eliminar-motocicletas/${id}`)
    console.log('Motocicleta eliminada con id', id);
    await cargarMotocicletas();
  }catch (err) {
    console.error('Error al eliminar la motocicleta', err);
    Swal.fire({
      icon: 'error',
      title: 'Error al eliminar la motocicleta',
      text: 'No se pudo eliminar la motocicleta'
    });
  }
}

onMounted(cargarMotocicletas);
</script>

<template>
  <div class="container">
    <div class="row">
      <div class="col-md-12 mt-4">
        <div class="card shadow p-4 mb-4">
          <h2 class="text-center">Formulario de Motocicletas</h2>
          <form @submit.prevent="agregarMotocicleta">
            <div class="row">
              <div class="col-md-6 mb-3">
                <label for="numeroSerie" class="form-label">Número de Serie</label>
                <input type="text" class="form-control" id="numeroSerie" v-model="nuevaMotocicleta.numeroSerie" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="marca" class="form-label">Marca</label>
                <input type="text" class="form-control" id="marca" v-model="nuevaMotocicleta.marca" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="modelo" class="form-label">Modelo</label>
                <input type="text" class="form-control" id="modelo" v-model="nuevaMotocicleta.modelo" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="cilindrada" class="form-label">Cilindrada (cc)</label>
                <input type="number" class="form-control" id="cilindrada" v-model="nuevaMotocicleta.cilindrada" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="color" class="form-label">Color</label>
                <input type="text" class="form-control" id="color" v-model="nuevaMotocicleta.color" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="año" class="form-label">Año</label>
                <input type="number" class="form-control" id="año" v-model="nuevaMotocicleta.año" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="precio" class="form-label">Precio ($)</label>
                <input type="number" step="0.01" class="form-control" id="precio" v-model="nuevaMotocicleta.precio" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="imagenURL" class="form-label">URL de Imagen</label>
                <input type="text" class="form-control" id="imagenURL" v-model="nuevaMotocicleta.imagenURL">
              </div>
            </div>

            <button type="submit" class="btn btn-primary">
              {{ editado ? 'Actualizar Motocicleta' : 'Agregar Motocicleta' }}
            </button>
          </form>
        </div>
      </div>
      
      <div class="col-md-12">
        <h2>Tabla de Motocicletas</h2>
        <table class="table">
          <thead>
            <tr>
              <th scope="col">ID</th>
              <th scope="col">N° Serie</th>
              <th scope="col">Marca</th>
              <th scope="col">Modelo</th>
              <th scope="col">Cilindrada</th>
              <th scope="col">Color</th>
              <th scope="col">Año</th>
              <th scope="col">Precio</th>
              <th scope="col">Imagen</th>
              <th scope="col">Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="motocicleta in motocicletas" :key="motocicleta.id">
              <td>{{ motocicleta.id }}</td>
              <td>{{ motocicleta.numeroSerie }}</td>
              <td>{{ motocicleta.marca }}</td>
              <td>{{ motocicleta.modelo }}</td>
              <td>{{ motocicleta.cilindrada }} cc</td>
              <td>{{ motocicleta.color }}</td>
              <td>{{ motocicleta.año }}</td>
              <td>${{ motocicleta.precio }}</td>
              <td><img :src="motocicleta.imagenURL" alt="Imagen de la motocicleta" width="50"></td>
              <td>
                <button @click="eliminarMotocicleta(motocicleta.id)" class="btn btn-danger mx-2">
                  <i class="bi bi-trash"></i> Borrar
                </button>
                <button @click="editarMotocicleta(motocicleta)" class="btn btn-warning">
                  <i class="bi bi-pencil-fill"></i> Editar
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<style scoped></style>