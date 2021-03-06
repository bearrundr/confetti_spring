package com.elevysi.site.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.elevysi.site.entity.Profile;
import com.elevysi.site.entity.Profile_;
import com.elevysi.site.entity.User;
import com.elevysi.site.entity.User_;
import com.elevysi.site.pojo.OffsetPage;
import com.elevysi.site.pojo.Page;
import com.elevysi.site.pojo.Page.SortDirection;

@Repository
@Transactional
public class ProfileDAOImplement implements ProfileDAO{
	
	
	@PersistenceContext
	private EntityManager em;
	
	public List<Profile> getProfiles(Page page){
		
//		org.hibernate.Filter filter = em.unwrap(Session.class).enableFilter("profilePictureFilter");
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Profile> criteria = cb.createQuery(Profile.class);
		Root<Profile> profileRoot = criteria.from(Profile.class);
		criteria.select(profileRoot);
		
		TypedQuery<Profile> query = page.createQuery(em, criteria, profileRoot);
		List<Profile> profiles =  query.getResultList();
		
		for(Profile profile : profiles){
//			Hibernate.initialize(profile.getProfileOwner());
//			Hibernate.initialize(profile.getUploads());
		}
		
		return profiles;
		
	}
	public long getCount(){
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		cq.select(cb.count(cq.from(Profile.class)));
		
		return em.createQuery(cq).getSingleResult();
		
	}
	public OffsetPage buildOffsetPage(int pageIndex, int size,  SingularAttribute sortField, SortDirection sortDirection){
		return new OffsetPage(pageIndex, size, getCount(), sortField, sortDirection, Profile_.created, Profile_.modified, Profile_.id, Profile_.name, Profile_.description);
	}

	public Profile update(Profile profile){
		return em.merge(profile);
	}
	
	public Profile findById(int id){
				
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Profile> criteria = cb.createQuery(Profile.class);
		Root<Profile> queryRoot = criteria.from(Profile.class);
		Predicate condition = cb.equal(queryRoot.get(Profile_.id), id);
		criteria.where(condition);
		TypedQuery<Profile> query = em.createQuery(criteria);
		Profile profile = query.getSingleResult();
		
		Hibernate.initialize(profile.getProfilePicture());
//		Hibernate.initialize(Profile.getProfiles());
		
		
		return profile;
	}
	
	public Profile findProfileByUser(User user){
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Profile> criteria = cb.createQuery(Profile.class);
		Root<Profile> queryRoot = criteria.from(Profile.class);
		Predicate condition = cb.equal(queryRoot.get(Profile_.user), user);
		criteria.where(condition);
		TypedQuery<Profile> query = em.createQuery(criteria);
		Profile profile = query.getSingleResult();
		
		Hibernate.initialize(profile.getProfilePicture());
		
		return profile;
		
	}
	
	public Profile getUserPrincipalProfile(User user){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Profile> criteria = cb.createQuery(Profile.class);
		Root<Profile> queryRoot = criteria.from(Profile.class);
		
		Predicate expression1 = cb.equal(queryRoot.get(Profile_.user), user);
		Predicate expression2 = cb.equal(queryRoot.get(Profile_.profile_type_id), 1);
		
		Predicate andCondition = cb.and(expression1, expression2);		
		criteria.where(andCondition);
		
		TypedQuery<Profile> query = em.createQuery(criteria);
		Profile profile = query.getSingleResult();
		
		Hibernate.initialize(profile.getProfilePicture());
		Hibernate.initialize(profile.getProfileType());
		
		return profile;
	}


}
