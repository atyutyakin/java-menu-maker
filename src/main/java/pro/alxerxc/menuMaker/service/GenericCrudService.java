package pro.alxerxc.menuMaker.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import pro.alxerxc.menuMaker.entity.Persistable;

import javax.transaction.Transactional;
import java.util.List;

@Getter
@Setter
public class GenericCrudService<E extends Persistable<ID>, ID> {
    private JpaRepository<E, ID> repository;
    private String repositoryClassName;

    public GenericCrudService(JpaRepository<E, ID> repository) {
        this.repository = repository;
        this.repositoryClassName = repository.getClass().getSimpleName();
    }

    public List<E> findAll(){
        return repository.findAll();
    }

    public E findById(ID id) {
        return repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(repositoryClassName + ": entity is not found by id " + id));
    }

    @Transactional
    public E add(E productToAdd) {
        if (productToAdd.hasId()) {
            throw new IllegalArgumentException(repositoryClassName + ": new entity should not have id");
        }
        return repository.save(productToAdd);
    }

    @Transactional
    public E update(E productToUpdate) {
        if (!productToUpdate.hasId()) {
            throw new IllegalArgumentException(repositoryClassName + ": entity for update should have id");
        } else if (!repository.existsById(productToUpdate.getId())) {
            throw new IllegalArgumentException(repositoryClassName + ": entity for update with id " +
                    productToUpdate.getId() + " not exists");
        }
        return repository.save(productToUpdate);
    }

    @Transactional
    public void deleteById(ID id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException(repositoryClassName + ": entity for deletion with id " + id + " not exists");
        }
        repository.deleteById(id);
    }

}