package com.elevysi.site.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.metamodel.SingularAttribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.elevysi.site.entity.Comment;
import com.elevysi.site.entity.Post;
import com.elevysi.site.entity.Profile;
import com.elevysi.site.entity.ProfileType;
import com.elevysi.site.entity.User;
import com.elevysi.site.pojo.OffsetPage;
import com.elevysi.site.pojo.Page.SortDirection;
import com.elevysi.site.repository.ProfileDAO;
import com.elevysi.site.repository.ProfileRepository;

@Service
public class ProfileService extends AbstractService{

		@Autowired
		private ProfileRepository profileRepository;
		
		@Autowired
		private ProfileTypeService profileTypeService;
		
		@Autowired
		private CommentService commentService;
		
		@Autowired
		private PostService postService;
		
		@Autowired
		private ProfileDAO profileDAO;
		
		private static final int PAGE_SIZE = 20;

		public Profile saveProfile(Profile profile) {
			Date now = new Date();
			profile.setCreated(now);
			profile.setModified(now);
			return profileRepository.save(profile);			
		}
		
		@PreAuthorize("#profile.id == principal.activeProfile.id || hasRole('ADMIN')")
		public Profile savedEditedProfile(Profile profile){
			Date now = new Date();
			profile.setModified(now);
			return profileDAO.update(profile);
		}

		public Profile findByUserAndProfileType(User user, ProfileType profileType) {
			return profileRepository.findByUserAndProfileType(user, profileType);
		}
		
		public List<Profile> findUserProfiles(User user){
			return profileRepository.findByUser(user);
		}

		public Set<Profile> findProfileFriends(Profile userProfile) {
			Set<Profile> following =  profileRepository.findProfileFriends(userProfile.getId());
			Set<Profile> followed = profileRepository.findInverseProfileFriends(userProfile.getId());
			followed.addAll(following);
		  return followed;
			
		}

		public void bucket(Profile requesting, Profile requested) {
			requesting.getFriends().add(requested);
			profileRepository.save(requesting);
			
		}
		
		
		public Profile findUserProfile(User user){
			ProfileType userProfileType = profileTypeService.findOne("user");
			return this.findByUserAndProfileType(user, userProfileType);
		}

		public Profile findOne(Integer id) {
			
			return profileRepository.findById(id);
		}
		
		public Profile findOne(String name) {
			
			return profileRepository.findByName(name);
		}
		
		public Profile findOneForView(Integer id, Integer limitPosts, Integer limitFriends) {
			
			return profileRepository.findById(id);
		}
		
		
		
		public void addComment(Profile profile, Comment comment, boolean set){
			if(comment != null){
				
				/**
				 * Using this approach because fecth type is lazy
				 * Eager would explode latency
				 */
				Set<Comment> profileComments = commentService.findByProfileComment(profile);
				profile.setComments(profileComments);
				
				if(profile.getComments().contains(comment)){
					profile.getComments().remove(comment);
					profile.getComments().add(comment);
					
				}else{
					profile.getComments().add(comment);
				}
				
				if(set){
					commentService.setProfile(comment, profile, false);
				}
			}
		}

		public List<Post> findLastPosts(Profile postOwner, int limit) {
			
			
		
			return null;
		}

		public List<Profile> findAll() {
				
			return profileRepository.findAll();
		}
		
		public Set<Profile> findFriends(Profile profile) {
			
			return profileRepository.findProfileFriends(profile.getId());
		}
		
		
		
		public OffsetPage buildOffsetPage(int pageIndex, int size,  SingularAttribute sortField, SortDirection sortDirection){
			return profileDAO.buildOffsetPage(pageIndex, size, sortField, sortDirection);
		}
		
		@PreAuthorize("hasRole('ADMIN')")
		public List<Profile> getProfiles(com.elevysi.site.pojo.Page page){
			return profileDAO.getProfiles(page);
		}
		
		@PreAuthorize("#profile.id == principal.activeProfile.id || hasRole('ADMIN')")
		public Profile updateProfile(Profile profile){
			Date now = new Date();
			profile.setModified(now);
			return profileDAO.update(profile);
			
		}
		
		@PostAuthorize("returnObject.id == principal.activeProfile.id || hasRole('ADMIN')")
		public Profile loadFullProfile(int id){
			return profileDAO.findById(id);
		}
		
		public Profile findProfileByUser(User user){
			return profileDAO.findProfileByUser(user);
		}
		
		public Profile getPrincipalProfile(User user) {
			return profileDAO.getUserPrincipalProfile(user);
		}
		
		
}
