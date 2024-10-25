package com.pgm.jpademo2.service;

import com.pgm.jpademo2.dto.BoardDTO;
import com.pgm.jpademo2.dto.PageRequestDTO;
import com.pgm.jpademo2.dto.PageResponseDTO;

public interface BoardService {
    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

}
