package it.uniroma3.progetto2020.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.progetto2020.model.Task;

public interface TaskRepository extends CrudRepository<Task, Long>{

}
