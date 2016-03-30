package services.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import utils.ObjectUtils;
import utils.exceptions.NotFoundException;
import utils.orm.HbUtils;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by eduardo on 12/03/15.
 */
@Named
@Transactional
public abstract class AbstractService < T, I extends Serializable > implements GenericService< T, I > {
	JpaRepository< T, I > repo;


	public AbstractService( JpaRepository< T, I > repo ) {
		this.repo = repo;
	}

	/**
	 * Creates a new set of elements.
	 *
	 * @param entity The information of the created element.
	 *
	 * @return The created element.
	 */

	@Transactional
	@Override
	public Iterable< T > save( Iterable< T > entity ) {
		return repo.save( entity );
	}

	/**
	 * Creates a new element.
	 *
	 * @param entity The information of the created element.
	 *
	 * @return The created element.
	 */

	@Transactional
	@Override
	public T save( T entity ) {
		return repo.save( entity );
	}

	/**
	 * Updates the information of a element.
	 *
	 * @param updated The information of the updated element.
	 *
	 * @return The updated element.
	 *
	 * @throws NotFoundException if no element is found with given id.
	 */
	@Transactional( rollbackFor = NotFoundException.class )
	@Override
	public T update( I id, T updated ) throws NotFoundException {
		T u = repo.findOne( id );

		if ( u == null ) {
			throw new NotFoundException("couldn't update object with id "+ id);
		}

		ObjectUtils.copyProperties( updated, u );

		u = repo.save( u );

		return u;

	}

	/**
	 * Deletes a person.
	 *
	 * @param id The id of the deleted person.
	 *
	 * @return The deleted person.
	 *
	 * @throws NotFoundException if no person is found with the given id.
	 */
	@Transactional( rollbackFor = NotFoundException.class )
	@Override
	public boolean delete( I id ) throws NotFoundException {
		T o = repo.getOne( id );
		if ( o == null ) {
			throw new NotFoundException("Couldn't delete object with id: "+id);
		} else {
			repo.delete( o );
		}

		return true;
	}

	/**
	 * Finds all persons.
	 *
	 * @return A list of persons.
	 */
	@Transactional( readOnly = true )
	@Override
	public List< T > findAll() {
		return repo.findAll();
	}

	/**
	 * Finds person by id.
	 *
	 * @param id The id of the wanted element.
	 *
	 * @return The found person. If no element is found, this method returns null.
	 */
	@Transactional( readOnly = true )
	@Override
	public T findById( I id ) {
		T o = repo.getOne( id );
		if ( o != null ) {
			return HbUtils.deproxy( o );
		} else {
			return null;
		}
	}


	// then use Spring BeanUtils to copy and ignore null

}
