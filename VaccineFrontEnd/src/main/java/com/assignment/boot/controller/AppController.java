package com.assignment.boot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.assignment.boot.model.Center;

@RestController
public class AppController {
	
	RestTemplate template = new RestTemplate();
	
	@GetMapping("/")
	public ModelAndView homepage() {
		return new ModelAndView("home");
	}
	
	@GetMapping("/allCenters")
	public ModelAndView allCenters() {
		List<Map<String,Object>> mapList = template.getForObject("http://localhost:8085/baseurl/api/v1/all", List.class);
		return getCenterView(mapList);
	}
	
	@GetMapping("/findByDistrict")
	public ModelAndView findByDistrict() {
		return new ModelAndView("distSearch");
	}
	@PostMapping("/distList")
	public ModelAndView districtList(HttpServletRequest request) {
		String district = request.getParameter("district");
		List<Map<String,Object>> mapList = template.getForObject("http://localhost:8085/baseurl/api/v1?district="+district, List.class);
		return getCenterView(mapList);
	}
	
	@GetMapping("/findByPincode")
	public ModelAndView findByPincode() {
		return new ModelAndView("pinSearch");
	}
	@PostMapping("/pinList")
	public ModelAndView pincodeList(HttpServletRequest request) {
		int pincode = Integer.parseInt(request.getParameter("pincode"));
		List<Map<String,Object>> mapList = template.getForObject("http://localhost:8085/baseurl/api/v1/"+pincode, List.class);
		return getCenterView(mapList);
	}
	
	
	//	Method to put all the filtered centers from the mapList into the allCenters.jsp
	//	and returning the corresponding ModelAndView
	public ModelAndView getCenterView(List<Map<String,Object>> mapList) {
		ModelAndView mav = new ModelAndView("allCenters");
		List<Center> centerList = new ArrayList<>();
		for(Map<String,Object> map:mapList) {
			Center center = new Center();
			center.setName((String)map.get("name"));
			center.setAddress((String)map.get("address"));
			center.setDistrict((String)map.get("district"));
			center.setState((String)map.get("state"));
			center.setPincode((int)map.get("pincode"));
			centerList.add(center);
		}
		mav.addObject("centerList",centerList);
		return mav;
	}
}
