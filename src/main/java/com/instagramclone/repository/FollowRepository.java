package com.instagramclone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.instagramclone.entity.Follow;
import com.instagramclone.entity.User;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long>{
	
	public List<Follow> findByFollowed(User user);
	
	public List<Follow> findByFollower(User user);
	
	boolean existsByFollowerAndFollowed(User a,User b);
	
	void deleteByFollowerAndFollowed(User a,User b);

}
