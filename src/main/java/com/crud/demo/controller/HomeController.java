package com.crud.demo.controller;

import com.crud.demo.model.Espacio;
import com.crud.demo.model.Reserva;
import com.crud.demo.repository.ReservaRepository;
import com.crud.demo.repository.PersonalRepository;
import com.crud.demo.repository.EspacioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import java.time.LocalDate;

@Controller
public class HomeController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private PersonalRepository personalRepository;

    @Autowired
    private EspacioRepository espacioRepository;

    // Muestra la página web principal
    @GetMapping("/")
    public String index() {
        return "index"; // Renderiza index.html de templates
    }

    // Recibe los datos de la reserva enviados por JavaScript o Formulario
    @PostMapping("/api/reservas")
    @ResponseBody
    public String guardarReserva(@ModelAttribute Reserva reserva) {
        reservaRepository.save(reserva);
        return "Reserva guardada con éxito en MySQL";
    }

    // Panel de Administración completo (Citas, Personal y Salas)
    @GetMapping("/admin")
    public String verPanelAdmin(Model model) {
        model.addAttribute("reservas", reservaRepository.findAll());
        model.addAttribute("personalList", personalRepository.findAll());
        model.addAttribute("espaciosList", espacioRepository.findAll());
        return "admin";
    }

    // Modificar Fecha y Hora de una cita existente
    @PostMapping("/admin/actualizar-cita/{id}")
    public String actualizarCita(@PathVariable Long id, @RequestParam LocalDate fecha, @RequestParam String hora) {
        Reserva reserva = reservaRepository.findById(id).orElse(null);
        if (reserva != null) {
            reserva.setFecha(fecha);
            reserva.setHora(hora);
            reservaRepository.save(reserva);
        }
        return "redirect:/admin";
    }

    // Eliminar una reserva por ID
    @GetMapping("/admin/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id) {
        reservaRepository.deleteById(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/espacios/toggle/{id}")
    public String cambiarEstadoEspacio(@PathVariable Long id) {
        Espacio espacio = espacioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido"));
        espacio.setDisponible(!espacio.getDisponible());
        espacioRepository.save(espacio);
        return "redirect:/admin";
    }
}