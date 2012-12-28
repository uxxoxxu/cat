/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.noday.cat.web.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import net.noday.cat.model.ext.Dwz;
import net.noday.core.web.BaseController;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * cat DwzManager
 *
 * @author <a href="http://www.noday.net">Noday</a>
 * @version , 2012-12-24
 * @since 
 */
@Controller @RequestMapping("/admin/tools/dwz")
public class DwzManager extends BaseController {

	private static Logger log = Logger.getLogger(DwzManager.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String gen() {
		return "admin/tools/dwz";
	}
	@RequestMapping(method = RequestMethod.POST)
	public Model gen(@RequestParam("url") String url, @RequestParam("alias") String alias, Model m) {
		try {
			String urlstr = "http://dwz.cn/create.php";
			URL requrl = new URL(urlstr);
			URLConnection conn = requrl.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			String param = String.format(Locale.CHINA, "url=%s&alias=%s", url, alias);
			out.write(param);
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = reader.readLine();
			//{"longurl":"http:\/\/www.hao123.com\/","status":0,"tinyurl":"http:\/\/dwz.cn\/1RIKG"}
			ObjectMapper mapper = new ObjectMapper();
			Dwz dwz = mapper.readValue(line, Dwz.class);
			if (dwz.getStatus() == 0) {
				responseData(m, dwz.getTinyurl());
			} else {
				responseMsg(m, false, dwz.getErr_msg());
			}
		} catch (MalformedURLException e) {
			log.error(e.getMessage(), e);
			responseMsg(m, false, e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			responseMsg(m, false, e.getMessage());
		}
		return m;
	}
}
