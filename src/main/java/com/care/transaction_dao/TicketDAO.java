package com.care.transaction_dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.care.transaction_dto.TicketDTO;

public class TicketDAO {
	private JdbcTemplate template;
	private TransactionTemplate transactionTemplate;
	
	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public int[] buyTicket(TicketDTO dto) {
		String sql_user = "insert into userticket(id, ticketNum) values(?,?)";
		String sql_system = "insert into systemticket(id, ticketNum) values(?,?)";
		int arr[] = new int[2];
		try {
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					// TODO Auto-generated method stub
					arr[0] = template.update(sql_user, ps -> {
						ps.setString(1, dto.getId());
						ps.setInt(2, dto.getTicketNum());
					});
					arr[1] = template.update(sql_system, ps -> {
						ps.setString(1, dto.getId());
						ps.setInt(2, dto.getTicketNum());
					});
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}

	public Map<String, ArrayList> resultTicket() {
		String sql_user = "select * from userticket";
		String sql_system = "select * from systemticket";
		Map<String, ArrayList> map = new HashMap<String, ArrayList>();
		ArrayList<TicketDTO> userTicket = null;
		ArrayList<TicketDTO> systemTicket = null;
		try {
			userTicket = (ArrayList<TicketDTO>)template.query(sql_user, new BeanPropertyRowMapper<TicketDTO>(TicketDTO.class));
			systemTicket = (ArrayList<TicketDTO>)template.query(sql_system, new BeanPropertyRowMapper<TicketDTO>(TicketDTO.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("userticket", userTicket);
		map.put("systemticket", systemTicket);
		return map;
	}
}
