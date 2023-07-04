package com.whale.web.codes.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.whale.web.codes.model.FormCodes;
import com.whale.web.codes.service.QRCodeService;

@Controller
@RequestMapping("/codes")
public class CodesController {
	
	@Autowired
	FormCodes form;
	
	@Autowired
	QRCodeService qrCodeService;
	
	@RequestMapping(value="/qrcode", method=RequestMethod.GET)
	public String certificateGenerator(Model model) {
		
		model.addAttribute("form", form);
		return "codes";
		
	}
	

	@PostMapping("/generateqrcode")
	public String processImage(FormCodes form, HttpServletResponse response) throws IOException{
		
		try {
			File processedImage = qrCodeService.generateQRCode(form.getLink());
			
			// Define o tipo de conteúdo e o tamanho da resposta
		    response.setContentType("image/png");
		    response.setContentLength((int) processedImage.length());
	
		    // Define os cabeçalhos para permitir que a imagem seja baixada
		    response.setHeader("Content-Disposition", "attachment; filename=\"ModifiedImage.png\"");
		    response.setHeader("Cache-Control", "no-cache");
			
			try (InputStream is = new FileInputStream(processedImage)){
				IOUtils.copy(is, response.getOutputStream());
			}catch(Exception e) {
				throw new RuntimeException("Error writing image in response.", e);
			}
		} catch(Exception e) {
			return "redirect:/codes/qrcode";
		}
		
		return null;
	}
}