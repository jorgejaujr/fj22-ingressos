package br.com.caelum.ingresso.validacao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import br.com.caelum.ingresso.model.Sessao;

public class GerenciadorDeSessao {
	
	private List<Sessao> sessoesDaSala;
	
	public GerenciadorDeSessao(List<Sessao> sessoesDaSala) {
		this.sessoesDaSala = sessoesDaSala;
	}
	
	public boolean cabe(Sessao novaSessao) {
		if(terminaAmanha(novaSessao)){
			return false;
		}
		return sessoesDaSala.stream().noneMatch(sessaoExistente->horarioIsConflitante(sessaoExistente, novaSessao));
	}
	
	public boolean terminaAmanha(Sessao sessao) {
		LocalDateTime terminoSessaoNova = getTerminoSessaoComDiaDeHoje(sessao);
		LocalDateTime ultimoSegundoDeHoje = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
		
		if(terminoSessaoNova.isAfter(ultimoSegundoDeHoje)) {
			return true;
			
		}
		return false;
	}
	
	public boolean horarioIsConflitante(Sessao sessaoExistente, Sessao sessaoNova) {
		
		LocalDateTime inicioSessaoExistente = getInicioSessaoComDiaDeHoje(sessaoExistente);
		LocalDateTime terminoSessaoExistente = getTerminoSessaoComDiaDeHoje(sessaoExistente);
		LocalDateTime inicioSessaoNova = getInicioSessaoComDiaDeHoje(sessaoNova);
		LocalDateTime terminoSessaoNova = getTerminoSessaoComDiaDeHoje(sessaoNova);
		
		boolean sessaoNovaTerminaAntesDeExistente = terminoSessaoNova.isBefore(inicioSessaoExistente);
		
		boolean sessaoNovaComecaDepoisDaExistente = terminoSessaoExistente.isBefore(inicioSessaoNova);
		
		if(sessaoNovaTerminaAntesDeExistente || sessaoNovaComecaDepoisDaExistente) {
			return false;
		}
		return true;
	}
	
	public LocalDateTime getTerminoSessaoComDiaDeHoje(Sessao sessao) {
		
		LocalDate hoje = LocalDate.now();
		
		return sessao.getHorario().atDate(hoje);
	}
	
	public LocalDateTime getInicioSessaoComDiaDeHoje(Sessao sessao) {
		LocalDateTime inicioSessaoNova = getInicioSessaoComDiaDeHoje(sessao);
		
		return inicioSessaoNova.plus(sessao.getFilme().getDuracao());
	}

}
