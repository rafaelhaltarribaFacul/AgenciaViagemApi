package br.com.senai.agenciaviagem.agenciaviagemapi.controller;

import br.com.senai.agenciaviagem.agenciaviagemapi.model.Destino;
import br.com.senai.agenciaviagem.agenciaviagemapi.model.Reserva;
import br.com.senai.agenciaviagem.agenciaviagemapi.service.DestinoService;
import br.com.senai.agenciaviagem.agenciaviagemapi.service.ReservaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/destinos")
public class DestinoController {

    private final DestinoService destinoService;
    private final ReservaService reservaService;

    public DestinoController(DestinoService destinoService,
                             ReservaService reservaService) {
        this.destinoService = destinoService;
        this.reservaService = reservaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Destino criar(@RequestBody Destino destino) {
        return destinoService.salvar(destino);
    }

    @GetMapping
    public List<Destino> listarTodos() {
        return destinoService.listarTodos();
    }

    @GetMapping("/buscar")
    public List<Destino> buscar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "local", required = false) String local) {
        return destinoService.buscar(nome, local);
    }

    @GetMapping("/{id}")
    public DetalhesDestinoResponse visualizar(@PathVariable Long id) {
        Destino dest = destinoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino não encontrado"));
        List<Reserva> reservas = reservaService.buscarPorDestinoId(id);
        return new DetalhesDestinoResponse(dest, reservas);
    }

    @PostMapping("/{id}/avaliacao/{nota}")
    public Destino avaliar(
            @PathVariable Long id,
            @PathVariable int nota) {

        if (nota < 1 || nota > 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A avaliação deve ser entre 1 e 10");
        }
        destinoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino não encontrado"));
        destinoService.adicionarAvaliacao(id, nota);
        return destinoService.buscarPorId(id).get();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirDestino(@PathVariable Long id) {
        boolean removed = destinoService.excluir(id);
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino não encontrado");
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reservas")
    @ResponseStatus(HttpStatus.CREATED)
    public Reserva criarReserva(
            @PathVariable Long id,
            @RequestBody RequisicaoReserva requisicao) {

        destinoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino não encontrado"));
        Reserva res = new Reserva();
        res.setNomeCliente(requisicao.getNomeCliente());
        res.setQuantidadePessoas(requisicao.getQuantidadePessoas());
        res.setDataViagem(LocalDate.parse(requisicao.getDataViagem()));
        return reservaService.salvar(id, res);
    }

    @GetMapping("/{id}/reservas")
    public List<Reserva> listarReservas(@PathVariable Long id) {
        destinoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino não encontrado"));
        return reservaService.buscarPorDestinoId(id);
    }

    @DeleteMapping("/{id}/reservas/{idReserva}")
    public ResponseEntity<Void> excluirReserva(
            @PathVariable Long id,
            @PathVariable Long idReserva) {

        destinoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino não encontrado"));
        boolean removed = reservaService.excluirPorId(idReserva);
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada");
        }
        return ResponseEntity.noContent().build();
    }

    public static class DetalhesDestinoResponse {
        private Long id;
        private String nome;
        private String local;
        private String descricao;
        private double avaliacaoMedia;
        private int quantidadeAvaliacoes;
        private List<Reserva> reservas;

        public DetalhesDestinoResponse(Destino dest, List<Reserva> reservas) {
            this.id = dest.getId();
            this.nome = dest.getNome();
            this.local = dest.getLocal();
            this.descricao = dest.getDescricao();
            this.avaliacaoMedia = dest.getAvaliacaoMedia();
            this.quantidadeAvaliacoes = dest.getQuantidadeAvaliacoes();
            this.reservas = reservas;
        }
        public Long getId() { return id; }
        public String getNome() { return nome; }
        public String getLocal() { return local; }
        public String getDescricao() { return descricao; }
        public double getAvaliacaoMedia() { return avaliacaoMedia; }
        public int getQuantidadeAvaliacoes() { return quantidadeAvaliacoes; }
        public List<Reserva> getReservas() { return reservas; }
    }

    public static class RequisicaoReserva {
        private String nomeCliente;
        private int quantidadePessoas;
        private String dataViagem;

        public String getNomeCliente() { return nomeCliente; }
        public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
        public int getQuantidadePessoas() { return quantidadePessoas; }
        public void setQuantidadePessoas(int quantidadePessoas) { this.quantidadePessoas = quantidadePessoas; }
        public String getDataViagem() { return dataViagem; }
        public void setDataViagem(String dataViagem) { this.dataViagem = dataViagem; }
    }
}
