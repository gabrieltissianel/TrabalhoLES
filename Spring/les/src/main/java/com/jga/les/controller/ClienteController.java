package com.jga.les.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jga.les.dtos.ClienteDto;
import com.jga.les.model.Cliente;
import com.jga.les.model.Compra;
import com.jga.les.service.ClienteService;
import com.jga.les.service.GenericService;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/cliente")
public class ClienteController extends GenericController<Cliente, Long> {

    public ClienteController(GenericService<Cliente, Long> genericApplication) {
        super("/cliente", genericApplication);
    }

    @GetMapping("/aniversario")
    @PreAuthorize("hasAuthority(#root.this.getNomeTela(''))")
    public ResponseEntity<List<Cliente>> relatorioAniversarioEntity() throws JRException {
        return ((ClienteService)genericService).findByAniversario();
    }

    @GetMapping("/compraaberta/{cartao}")
    @PreAuthorize("hasAuthority(#root.this.getNomeTela('/compraaberta'))")
    public ResponseEntity<Compra> relatorioCompraAberta(@PathVariable String cartao) throws JRException {
        return ((ClienteService)genericService).findCompraAberta(cartao);
    }

    @GetMapping("cartao/{cartao}")
    @PreAuthorize("hasAuthority(#root.this.getNomeTela(''))")
    public ResponseEntity<Cliente> getClienteByCartao(@PathVariable String cartao) throws JRException {
        return ((ClienteService)genericService).findClienteByCartao(cartao);
    }
}