package wang.kingweb.community.service;

import org.springframework.stereotype.Service;
import wang.kingweb.community.dto.PaginationDTO;

@Service
public class PaginationService {

    public PaginationDTO getPageInfo(Long page, Long size, Long totalCount) {
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(page,size,totalCount);
        return paginationDTO;
    }
}
