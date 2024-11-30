const express = require('express');
const bodyParser = require('body-parser');
const { Pool } = require('pg');
const cors = require('cors');

const app = express();
const port = 3000;

// Configurar PostgreSQL
const pool = new Pool({
    user: 'postgres',
    host: 'localhost', 
    database: 'info_idgs101d',
    password: 'utNay',
    port: 5432, // Puerto de PostgreSQL
});

// Middleware
app.use(cors());
app.use(bodyParser.json());

// Ruta para insertar datos
app.post('/insert', async (req, res) => {
    console.log(req.body); // Log para depurar los datos recibidos

    // Extraer los datos del cuerpo de la solicitud
    const { nombres, primer_apellido, segundo_apellido, edad, fecha_nacimiento, hora, fecha_registro, estado } = req.body;

    try {
        // Consulta SQL para insertar datos
        const query = `
            INSERT INTO informacion (nombres, primer_apellido, segundo_apellido, edad, fecha_nacimiento, hora, fecha_registro, estado)
            VALUES ($1, $2, $3, $4, $5, $6, $7, $8) RETURNING *;
        `;

        // Valores que se usarán en la consulta
        const values = [nombres, primer_apellido, segundo_apellido, edad, fecha_nacimiento, hora, fecha_registro, estado];

        // Ejecutar la consulta
        const result = await pool.query(query, values);
        res.json(result.rows[0]); // Devolver la fila insertada
    } catch (error) {
        console.error(error);
        res.status(500).send('Error al insertar los datos');
    }
});

// Ruta para obtener datos
app.get('/data', async (req, res) => {
    try {
        const result = await pool.query('SELECT * FROM informacion');
        console.log('Datos enviados al cliente:', result.rows); // Log para verificar
        res.json(result.rows); // Enviar los datos
    } catch (error) {
        console.error(error);
        res.status(500).send('Error al obtener los datos');
    }
});

// Iniciar el servidor
app.listen(port, () => {
    console.log(`Servidor escuchando en http://localhost:${port}`);
});
