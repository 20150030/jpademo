package com.pgm.jpademo2.service;

import com.pgm.jpademo2.dto.PageRequestDTO;
import com.pgm.jpademo2.dto.PageResponseDTO;
import com.pgm.jpademo2.dto.ReplyDTO;

public interface ReplyService {
    Long register(ReplyDTO replyDTO);

    ReplyDTO read(Long rno);

    void modify(ReplyDTO replyDTO);

    void remove(Long rno);

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);

}
