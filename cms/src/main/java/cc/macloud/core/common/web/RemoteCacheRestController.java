package cc.macloud.core.common.web;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.macloud.core.cache.service.CacheService;
import cc.macloud.core.common.utils.StringUtils;

@RestController
@RequestMapping("/api/remote-cache")
public class RemoteCacheRestController implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(RemoteCacheRestController.class);

    private ApplicationContext ctx;

    private String allowRegExp;
    private Pattern allowPattern;

    public void setAllowRegExp(String allowRegExp) {
        this.allowRegExp = allowRegExp;
        if (StringUtils.isNotBlank(allowRegExp)) {
            this.allowPattern = Pattern.compile(allowRegExp, Pattern.CASE_INSENSITIVE);
        } else {
            this.allowPattern = null;
        }
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/clean")
    public ResponseEntity<Map<String, Object>> clean(
            @RequestParam(value = "cache", required = false) String cache,
            HttpServletRequest request) {

        String callerIP = request.getRemoteAddr();
        if (!allow(callerIP)) {
            Map<String, Object> body = new LinkedHashMap<String, Object>();
            body.put("success", Boolean.FALSE);
            body.put("message", "not allow IP (" + callerIP + ")");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
        }

        int cleaned = 0;
        try {
            if (ctx == null) {
                Map<String, Object> body = new LinkedHashMap<String, Object>();
                body.put("success", Boolean.FALSE);
                body.put("message", "spring config fail, ctx is null");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
            }

            if (StringUtils.isNotBlank(cache)) {
                CacheService cacheObj = ctx.getBean(cache, CacheService.class);
                if (cacheObj != null) {
                    cacheObj.destroy();
                    cleaned = 1;
                    logger.warn("cache({}) clean", cache);
                }
            } else {
                String[] cacheNames = ctx.getBeanNamesForType(CacheService.class);
                for (String name : cacheNames) {
                    CacheService cacheObj = ctx.getBean(name, CacheService.class);
                    if (cacheObj != null) {
                        cacheObj.destroy();
                        cleaned++;
                        logger.warn("cache({}) clean", name);
                    }
                }
            }

            Map<String, Object> body = new LinkedHashMap<String, Object>();
            body.put("success", Boolean.TRUE);
            body.put("cleaned", Integer.valueOf(cleaned));
            body.put("cache", cache);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            logger.error("clean cache false", e);
            Map<String, Object> body = new LinkedHashMap<String, Object>();
            body.put("success", Boolean.FALSE);
            body.put("message", "clean cache false");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    private boolean allow(String callerIP) {
        if (allowPattern == null) {
            return true;
        }
        Matcher matcher = allowPattern.matcher(callerIP);
        return matcher.find();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}
